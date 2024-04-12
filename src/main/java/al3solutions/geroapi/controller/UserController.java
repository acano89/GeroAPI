package al3solutions.geroapi.controller;

import al3solutions.geroapi.model.ERole;
import al3solutions.geroapi.model.Role;
import al3solutions.geroapi.model.User;
import al3solutions.geroapi.payload.request.ChangePasswordRequest;
import al3solutions.geroapi.payload.response.MessageResponse;
import al3solutions.geroapi.repository.RoleRepository;
import al3solutions.geroapi.repository.UserRepository;
import al3solutions.geroapi.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // Add role to existing user
    @PutMapping("/add-role/{username}/{newRole}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> giveRoleToUser(@PathVariable String username, @PathVariable String newRole) {

        Optional<ERole> eRole = getRoleNames(newRole);

        if(eRole.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid role: " + newRole));
        }

        Optional<Role> role = roleRepository.findByName(ERole.valueOf(newRole));

        System.out.println(role.get().getName().toString());
        User updatedUser = userRepository.findByUsername(username)
                .map(user -> {
                    user.addRole(role.get());
                    System.out.println(user.getEmail());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return ResponseEntity.ok(updatedUser);

    }


    @PutMapping("/remove-role/{username}/{removeRole}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> removeRoleFromUser(@PathVariable String username, @PathVariable String removeRole) {

        Optional<ERole> eRole = getRoleNames(removeRole);


        if(eRole.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid role: " + removeRole));
        }

        Optional<Role> role = roleRepository.findByName(ERole.valueOf(removeRole));

        User updatedUser = userRepository.findByUsername(username)
                .map(user -> {
                    user.removeRole(role.get());
                    System.out.println(user.getEmail());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return ResponseEntity.ok(updatedUser);

    }

    /** Metodo para cambiar de contraseña que no compila, TODO
    @PostMapping("/change-password/{username}/{changedPassword}")
    public ResponseEntity<?> changePassword(@PathVariable String username, @PathVariable String changedPassword) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));


        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("La contraseña actual es incorrecta"));
        }


        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Contraseña actualizada correctamente"));
    }
    */
    private static Optional<ERole> getRoleNames(String removeRole) {
        return Arrays.stream(ERole.values())
                .filter(i -> i.name().equalsIgnoreCase(removeRole))
                .findFirst();
    }
}
