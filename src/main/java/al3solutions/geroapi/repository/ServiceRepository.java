package al3solutions.geroapi.repository;

import al3solutions.geroapi.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, Long> {

    Optional<Service> findByName(String name);

    //Optional<Service> findByDate(Date date);
}
