package al3solutions.geroapi.controller;

import al3solutions.geroapi.model.*;
import al3solutions.geroapi.payload.request.*;
import al3solutions.geroapi.payload.response.MessageResponse;
import al3solutions.geroapi.payload.response.UsersListResponse;
import al3solutions.geroapi.repository.FamiliarRepository;
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

import java.util.*;
import java.util.stream.Collectors;
import java.sql.Date;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ServiceRepository serviceRepository;
    private final FamiliarRepository familiarRepository;

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
            return ResponseEntity.badRequest().body(new MessageResponse("La contrasenya actual es incorrecta"));
        }

        updatedUser.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(updatedUser);

        return ResponseEntity.ok(new MessageResponse("Contrasenya actualitzada correctament"));
    }

    //Modifica el nom a un usuari existent. Not work TODO
    @PostMapping("/change-name")
    public ResponseEntity<?> changeUsername(@Valid @RequestBody ChangeUsernameRequest changeUsernameRequest){
        User updatedUser = userRepository.findByUsername(changeUsernameRequest.getCurrentName())
                .orElseThrow(()-> new UsernameNotFoundException(changeUsernameRequest.getCurrentName()));

        if(!userRepository.findByUsername(changeUsernameRequest.getNewName()).isPresent()){
            updatedUser.setUsername(changeUsernameRequest.getNewName());
            userRepository.save(updatedUser);
            return ResponseEntity.ok(new MessageResponse("Nom actualitzat correctament"));
        }

        return ResponseEntity.ok(new MessageResponse("El nom ja existeix."));
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
                .map(user -> new UsersListResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRoles()))
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(userInfoList);
    }




    //Métode per crear familiar resident

    @PostMapping("/set-Familiar")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> setFamiliar(@Valid @RequestBody SetFamiliarRequest setFamiliarRequest){

        Familiar familiar = Familiar.builder()
                .name(setFamiliarRequest.getName())
                .familiarsUserId((Set<User>) setFamiliarRequest.getFamiliarUser())
                .familiarMail(setFamiliarRequest.getFamiliarMail())
                .state(setFamiliarRequest.getState())
                .reason(setFamiliarRequest.getReason())
                .place(setFamiliarRequest.getPlace())
                .dayTrip(setFamiliarRequest.getDayTrip())
                .shower(setFamiliarRequest.getShower())
                .pickup(setFamiliarRequest.getPickup())
                .build();

        familiarRepository.save(familiar);
        return ResponseEntity.ok().body(familiar);
    }
}
