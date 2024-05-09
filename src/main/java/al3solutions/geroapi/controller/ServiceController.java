package al3solutions.geroapi.controller;

import al3solutions.geroapi.model.Service;
import al3solutions.geroapi.payload.request.GetServiceRequest;
import al3solutions.geroapi.payload.request.SetServiceRequest;
import al3solutions.geroapi.repository.ServiceRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/service")
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceRepository serviceRepository;

    //Ingresa un servei.
    @PostMapping("/set-Service")
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

    //Consulta un servei amb data y nom de Familiar
    @PostMapping("/get-Service")
    public ResponseEntity<?> getService(@Valid @RequestBody GetServiceRequest getServiceRequest){

        //Extreu la fecha y el nom
        String date = getServiceRequest.getDate();
        String name = getServiceRequest.getName();

        // Buscar los servicios por fecha y usuario en la base de datos
        List<Service> services = serviceRepository.findByDateAndName(date, name);

        // Devolver los servicios encontrados como respuesta
        return ResponseEntity.ok().body(services);
    }
}
