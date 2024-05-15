package al3solutions.geroapi.controller;

import al3solutions.geroapi.model.Service;
import al3solutions.geroapi.payload.request.GetServiceRequest;
import al3solutions.geroapi.payload.request.SetServiceRequest;
import al3solutions.geroapi.payload.response.MessageResponse;
import al3solutions.geroapi.repository.ServiceRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/service")
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceRepository serviceRepository;

    //Ingresa un servei.
    @PostMapping("/set-Service")
    public ResponseEntity<?> setService(@Valid @RequestBody SetServiceRequest setServiceRequest){

        String day = setServiceRequest.getDate();
        String name = setServiceRequest.getName();

        if (serviceRepository.existsByDateAndName(day,name)){
            return ResponseEntity.ok(new MessageResponse("El servei del día "+day+ " per en "+name+" ja exiteix" ));
        }

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

    //Consulta un servei amb data y nom de Familiar
    @PostMapping("/get-Service")
    public ResponseEntity<?> getService(@Valid @RequestBody GetServiceRequest getServiceRequest){

        //Extreu la fecha y el nom
        String date = getServiceRequest.getDate();
        String name = getServiceRequest.getName();

        if (!serviceRepository.existsByDateAndName(date,name)){
            return ResponseEntity.ok(new MessageResponse("No hi han dades del dia "+date+ " per en "+name+"."));
        }

        // Buscar los servicios por fecha y usuario en la base de datos
        Optional<Service> optionalService = serviceRepository.findByDateAndName(date, name);

        Service service = optionalService.get();

        // Devolver los servicios encontrados como respuesta
        return ResponseEntity.ok().body(service);
    }

    //Modifica les dades d'un servei amb dades i nom
    @PostMapping("/update-Service")
    public ResponseEntity<?> updateService (@Valid @RequestBody SetServiceRequest setServiceRequest){

        String day = setServiceRequest.getDate();
        String name = setServiceRequest.getName();

        if (!serviceRepository.existsByDateAndName(day,name)){
            return ResponseEntity.ok(new MessageResponse("No hi han dades del dia "+day+ " per en "+name+"."));
        }

        Optional<Service> optionalService = serviceRepository.findByDateAndName(day, name);

        Service updateService = optionalService.get();

        updateService.setName(setServiceRequest.getName());
        updateService.setDate(setServiceRequest.getDate());
        updateService.setBreakfast(setServiceRequest.getBreakfast());
        updateService.setLunch(setServiceRequest.getLunch());
        updateService.setSnack(setServiceRequest.getSnack());
        updateService.setDiaperNum(setServiceRequest.getDiaperNum());
        updateService.setShower(setServiceRequest.getShower());
        updateService.setUrination(setServiceRequest.getUrination());
        updateService.setDeposition(setServiceRequest.getDeposition());

        serviceRepository.save(updateService);

        return ResponseEntity.ok().body(updateService);

    }

    @PostMapping("/delete-Service/{name}/{date}")
    public ResponseEntity<?> deleteService(@PathVariable String name, @PathVariable String date){

        if (!serviceRepository.existsByDateAndName(date,name)){
            return ResponseEntity.ok(new MessageResponse("No hi han dades del dia "+date+ " per en "+name+"."));
        }

        Optional<Service> optionalService = serviceRepository.findByDateAndName(date,name);

        Service service = optionalService.get();

        serviceRepository.delete(service);
        return ResponseEntity.ok(new MessageResponse("Servei del "+date +" d'en "+ name+" eliminat correctament"));
    }
}
