package al3solutions.geroapi;

import al3solutions.geroapi.model.ERole;
import al3solutions.geroapi.model.Role;
import al3solutions.geroapi.model.User;
import al3solutions.geroapi.repository.RoleRepository;
import al3solutions.geroapi.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class GeroApiApplication implements CommandLineRunner {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	public GeroApiApplication(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public static void main(String[] args) {
		SpringApplication.run(GeroApiApplication.class, args);
	}


	// Add a default ADMIN user to the system for testing purposes
	@Override
	public void run(String... args) throws Exception {

		// Add roles if not already in the DB
		if (!roleRepository.existsById(1L)) {
			roleRepository.save(new Role(1, ERole.ROLE_ADMIN));
		}

		if (!roleRepository.existsById(2L)) {
			roleRepository.save(new Role(2, ERole.ROLE_CARE));
		}

		if (!roleRepository.existsById(3L)) {
			roleRepository.save(new Role(3, ERole.ROLE_USER));
		}

		if (!roleRepository.existsById(4L)) {
			roleRepository.save(new Role(4, ERole.ROLE_INACTIVE));
		}



		Role role = roleRepository.findByName(ERole.ROLE_ADMIN).get();
		Set<Role> roles = Set.of(role);

		User user = User.builder()
				.username("root")
				.email("root@centreenxarxa.com")
				.password(passwordEncoder.encode("root"))
				.roles(roles)
				.build();

		if (!userRepository.existsByUsername(user.getUsername())) {
			userRepository.save(user);
		}
	}
}