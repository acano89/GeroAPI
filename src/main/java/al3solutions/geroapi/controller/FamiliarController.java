package al3solutions.geroapi.controller;

import al3solutions.geroapi.model.Familiar;
import al3solutions.geroapi.model.User;
import al3solutions.geroapi.payload.request.GetFamiliarRequest;
import al3solutions.geroapi.payload.request.SetFamiliarRequest;
import al3solutions.geroapi.payload.response.MessageResponse;
import al3solutions.geroapi.repository.FamiliarRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/familiar")
@RequiredArgsConstructor
public class FamiliarController {

    private final FamiliarRepository familiarRepository;

    //Ingresa dades de familiar resident
    @PostMapping("/set-Familiar")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> setFamiliar(@Valid @RequestBody SetFamiliarRequest setFamiliarRequest){

        Familiar familiar = Familiar.builder()
                .name(setFamiliarRequest.getName())
                .familiarsUserName(setFamiliarRequest.getFamiliarsUserName())
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

    //Regresa dades de familiar resident amb nom de resident i de familiar
    @PostMapping("/get-familiar")
    public ResponseEntity<?> getFamiliar(@Valid @RequestBody GetFamiliarRequest getFamiliarRequest){

        String name = getFamiliarRequest.getName();
        String familiarsUserName = getFamiliarRequest.getFamiliarsUserName();

        List<Familiar> familiars = familiarRepository.findByNameAndFamiliarsUserName(name, familiarsUserName);

        if (!familiars.isEmpty()){
            return ResponseEntity.ok().body(familiars);
        }else {
            return ResponseEntity.badRequest().body(new MessageResponse("No hi ha relació entre "+familiarsUserName
                    + " i "+ name));
        }

    }
}
