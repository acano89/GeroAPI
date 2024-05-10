package al3solutions.geroapi.controller;

import al3solutions.geroapi.model.ERole;
import al3solutions.geroapi.model.Role;
import al3solutions.geroapi.model.User;
import al3solutions.geroapi.payload.request.ChangeMailRequest;
import al3solutions.geroapi.payload.request.ChangePasswordRequest;
import al3solutions.geroapi.payload.request.ChangeUsernameRequest;
import al3solutions.geroapi.payload.request.UpdateUserRequest;
import al3solutions.geroapi.payload.response.MessageResponse;
import al3solutions.geroapi.payload.response.UserInfoResponse;
import al3solutions.geroapi.payload.response.UsersListResponse;
import al3solutions.geroapi.repository.RoleRepository;
import al3solutions.geroapi.repository.UserRepository;
import al3solutions.geroapi.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    //Afegeix Role a un usuari existent
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

    //Esborra Role a un usuari existent
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

    //Modifica la contrasenya a un usuari existent
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()){
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails){

                UserDetailsImpl userDetails = (UserDetailsImpl) principal;

                String username = userDetails.getUsername();
                String password = userDetails.getPassword();

                User updatedUser = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException(username));

                if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), password)) {
                    return ResponseEntity.badRequest().body(new MessageResponse("La contrasenya actual es incorrecta"));
                }

                updatedUser.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
                userRepository.save(updatedUser);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new MessageResponse("Contrasenya actualitzada correctament"));
    }

    //Modifica el nom del propi usuari
    @PostMapping("/change-name")
    public ResponseEntity<?> changeUsername(@Valid @RequestBody ChangeUsernameRequest changeUsernameRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {

                UserDetailsImpl userDetails = (UserDetailsImpl) principal;

                String username = userDetails.getUsername();

                User updatedUser = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException(username));

                if (changeUsernameRequest.getNewName().equalsIgnoreCase(username)) {
                    return ResponseEntity.badRequest().body(new MessageResponse("Es el mateix nom."));
                }

                if (userRepository.existsByUsername(changeUsernameRequest.getNewName())){
                    return ResponseEntity.badRequest().body(new MessageResponse("Aquest username ja existeix"));
                }

                if (!changeUsernameRequest.getCurrentName().equalsIgnoreCase(username)){
                    return ResponseEntity.badRequest().body(new MessageResponse("Aquest no es el teu nom actual!"));

                }
                updatedUser.setUsername(changeUsernameRequest.getNewName());
                userRepository.save(updatedUser);
                return ResponseEntity.ok(new MessageResponse("Nom actualitzat correctament"));
            }
        }
        return null;
    }


    //Modifica l'email d'un usuari existent
    @PostMapping("/change-email")
    public ResponseEntity<?> changeMail(@Valid @RequestBody ChangeMailRequest changeMailRequest){
            User updatedUser = userRepository.findByUsername(changeMailRequest.getUsername())
                    .orElseThrow(()-> new UsernameNotFoundException(changeMailRequest.getUsername()));

            updatedUser.setEmail(changeMailRequest.getNewEmail());
            userRepository.save(updatedUser);

        return ResponseEntity.ok(new MessageResponse("Email canviat correctament"));
    }

    private static Optional<ERole> getRoleNames(String removeRole) {
        return Arrays.stream(ERole.values())
                .filter(i -> i.name().equalsIgnoreCase(removeRole))
                .findFirst();
    }

    //Llista els usuaris de l'aplicació
    @PostMapping("/users-list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UsersListResponse>> usersList() {
        List<User> users = userRepository.findAll();
        List<UsersListResponse> userInfoList = users.stream()
                .map(user -> new UsersListResponse(user.getId(), user.getUsername(),
                        user.getEmail(), user.getRoles()))
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(userInfoList);
    }

    @DeleteMapping("/delete/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable String username){

        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException(username));

        userRepository.delete(user);
        return ResponseEntity.ok(new MessageResponse("Usuari "+username +" eliminat correctament"));
    }

    @PostMapping("/updateUser/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserRequest updateUserRequest,@PathVariable String username){

        User updatedUser = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException(username));

        if(!userRepository.existsByUsername(updateUserRequest.getNewName())){
            updatedUser.setUsername(updateUserRequest.getNewName());
        }else {
            return ResponseEntity.ok(new MessageResponse("El nom ja existeix."));
        }

        if (!userRepository.existsByEmail(updateUserRequest.getNewEmail())){
            updatedUser.setEmail(updateUserRequest.getNewEmail());
        } else {
            return ResponseEntity.ok(new MessageResponse("El mail ja existeix per un altre usuari."));

        }

        userRepository.save(updatedUser);

        return ResponseEntity.ok(new MessageResponse("Usuari "+username +" modificat correctament"));
    }

    @PostMapping("/updateUserPassword/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateUserPassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest,@PathVariable String username){

        User updatedUser = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException(username));

        //Control de que la contraseña actual es correcta.
        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), updatedUser.getPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("La contrasenya actual es incorrecta"));
        }

        updatedUser.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(updatedUser);

        return ResponseEntity.ok(new MessageResponse("Contrasenya actualitzada correctament"));
    }

    @PostMapping("/userInfo/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> userInfo(@PathVariable String username){

        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException(username));;

        List<String> roles = new ArrayList<>();
        for (Role role : user.getRoles()) {
            ERole name = role.getName();
            roles.add(String.valueOf(name));
        }

        return ResponseEntity.ok()
                .body(UserInfoResponse
                        .builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .roles(roles)
                        .build());
    }
}