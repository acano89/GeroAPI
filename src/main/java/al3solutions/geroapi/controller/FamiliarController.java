package al3solutions.geroapi.controller;

import al3solutions.geroapi.model.Familiar;
import al3solutions.geroapi.model.User;
import al3solutions.geroapi.payload.request.GetFamiliarRequest;
import al3solutions.geroapi.payload.request.SetFamiliarRequest;
import al3solutions.geroapi.payload.request.UpdateFamiliarRequest;
import al3solutions.geroapi.payload.response.MessageResponse;
import al3solutions.geroapi.repository.FamiliarRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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

    @PostMapping("/updateFamiliar/{name}")
    //Modifica dades de familiar resident.
    public ResponseEntity<?> updateFamiliar(@Valid @RequestBody UpdateFamiliarRequest updateFamiliarRequest, @PathVariable String name){


        Optional<Familiar> toUpdate = familiarRepository.findByName(name);

        if (toUpdate.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse("No existeix "+ name+" encara."));
        }

        Familiar updateFamiliar = toUpdate.get();

        updateFamiliar.setName(updateFamiliarRequest.getName());
        updateFamiliar.setFamiliarsUserName(updateFamiliarRequest.getFamiliarsUserName());
        updateFamiliar.setFamiliarMail(updateFamiliarRequest.getFamiliarMail());
        updateFamiliar.setState(updateFamiliarRequest.getState());
        updateFamiliar.setReason(updateFamiliarRequest.getReason());
        updateFamiliar.setPlace(updateFamiliarRequest.getPlace());
        updateFamiliar.setDayTrip(updateFamiliarRequest.getDayTrip());
        updateFamiliar.setShower(updateFamiliarRequest.getShower());
        updateFamiliar.setPickup(updateFamiliarRequest.getPickup());

        familiarRepository.save(updateFamiliar);
        return ResponseEntity.ok().body(updateFamiliar);
    }

    @DeleteMapping("/deleteFamiliar/{name}")
    public ResponseEntity<?> deleteFamiliar (@PathVariable String name){

        Optional<Familiar> optionalFamiliar  = familiarRepository.findByName(name);

        if(!optionalFamiliar.isPresent()){
            return ResponseEntity.ok(new MessageResponse("Familiar "+name +" no existeix."));
        }

        Familiar familiar = optionalFamiliar.get();
        familiarRepository.delete(familiar);

        return ResponseEntity.ok(new MessageResponse("Familiar "+name +" eliminat correctament"));
    }
}
