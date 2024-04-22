package al3solutions.geroapi.repository;

import al3solutions.geroapi.model.Familiar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FamiliarRepository extends JpaRepository<Familiar,Long> {
    Optional<Familiar> findByName (String name);

    Boolean existsByName(String name);

}
