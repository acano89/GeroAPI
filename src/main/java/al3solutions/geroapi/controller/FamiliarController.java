package al3solutions.geroapi.controller;

import al3solutions.geroapi.model.Familiar;
import al3solutions.geroapi.model.User;
import al3solutions.geroapi.payload.request.GetFamiliarRequest;
import al3solutions.geroapi.payload.request.SetFamiliarRequest;
import al3solutions.geroapi.repository.FamiliarRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/familiar")
@RequiredArgsConstructor
public class FamiliarController {

    private final FamiliarRepository familiarRepository;

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

    //TODO
    public ResponseEntity<?> getFamiliar(@Valid @RequestBody GetFamiliarRequest getFamiliarRequest){

        Optional<Familiar> familiar = familiarRepository.findByName(getFamiliarRequest.getName());

        if (familiar.isPresent()){

        }
    }
}
