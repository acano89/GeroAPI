package al3solutions.geroapi.repository;

import al3solutions.geroapi.model.Familiar;
import al3solutions.geroapi.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, Long> {

    Optional<Service> findByName(Familiar name);

    List<Service> findByDateAndName(String date, String name);
}
