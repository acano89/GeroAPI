package al3solutions.geroapi.repository;

import al3solutions.geroapi.model.ERole;
import al3solutions.geroapi.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName (ERole name);
}
