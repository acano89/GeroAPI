package al3solutions.geroapi.controller;

import al3solutions.geroapi.model.ERole;
import al3solutions.geroapi.model.Role;
import al3solutions.geroapi.model.Service;
import al3solutions.geroapi.model.User;
import al3solutions.geroapi.payload.request.ChangePasswordRequest;
import al3solutions.geroapi.payload.request.SetServiceRequest;
import al3solutions.geroapi.payload.response.MessageResponse;
import al3solutions.geroapi.payload.response.UsersListResponse;
import al3solutions.geroapi.repository.RoleRepository;
import al3solutions.geroapi.repository.ServiceRepository;
import al3solutions.geroapi.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ServiceRepository serviceRepository;

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

    //Metodo para cambiar de contraseña
    @PostMapping("/change-password/{username}")
    public ResponseEntity<?> changePassword(@PathVariable String username, @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {

        User updatedUser = userRepository.findByUsername(username)
                .map(user -> {
                    user.getPassword();
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new UsernameNotFoundException(username));

        //Control de que la contraseña actual es correcta.
        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), updatedUser.getPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("La contraseña actual es incorrecta"));
        }


        updatedUser.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(updatedUser);

        return ResponseEntity.ok(new MessageResponse("Contraseña actualizada correctamente"));
    }

    private static Optional<ERole> getRoleNames(String removeRole) {
        return Arrays.stream(ERole.values())
                .filter(i -> i.name().equalsIgnoreCase(removeRole))
                .findFirst();
    }

    //Métode per llistar els usuaris de l'aplicació. No funcional
    @PostMapping("/users-list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UsersListResponse>> usersList() {
        List<User> users = userRepository.findAll();
        List<UsersListResponse> userInfoList = users.stream()
                .map(user -> new UsersListResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRoles()))
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(userInfoList);
    }

    //Métode per guardar servei. No funcional
    @PostMapping("/set-Service")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> setService(@Valid @RequestBody SetServiceRequest setServiceRequest){


        Service service = Service.builder()
                .name(setServiceRequest.getName())
                .date(setServiceRequest.getDate())
                .breakfast(setServiceRequest.getBreakfast())
                .lunch(setServiceRequest.getLunch())
                .snack(setServiceRequest.getSnack())
                .diaperNum(setServiceRequest.getDiaperNum())
                .shower(setServiceRequest.getShower())
                .urination(setServiceRequest.getUrination())
                .deposition(setServiceRequest.getDeposition())
                .build();

        serviceRepository.save(service);
        return ResponseEntity.ok().body(service);

    }
}
