package com.tall.GestionUnchk;

import com.tall.GestionUnchk.entity.RoleEntity;
import com.tall.GestionUnchk.entity.User;
import com.tall.GestionUnchk.enums.Role;
import com.tall.GestionUnchk.repository.RoleRepository;
import com.tall.GestionUnchk.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;




@SpringBootApplication
public class GestionUnchkApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionUnchkApplication.class, args);
	}

	@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner initDatabase() {
        return args -> {

            if (userRepository.findByEmail("admin@unchk.edu.sn").isEmpty()) {

                RoleEntity adminRole = roleRepository
                        .findByName(Role.ADMIN)
                        .orElseThrow(() ->
                                new RuntimeException("Rôle ADMIN introuvable"));

                User admin = new User();

                admin.setUsername("admin");
                admin.setEmail("admin@unchk.edu.sn");
                admin.setFirstName("Super");
                admin.setLastName("Admin");

                admin.setPassword(
                        passwordEncoder.encode("Admin@2026")
                );

                admin.setIsActive(true);
                admin.setIsEnabled(true);

                admin.getRoles().add(adminRole);

                userRepository.save(admin);

                System.out.println(
                        "✅ Administrateur créé avec succès"
                );
            }
        };
    }
}

	/**
	 * Configure le service UserDetailsService pour Spring Security
	 * Utilisé pour charger les détails de l'utilisateur depuis la base de données
	 */
	@Bean
	public UserDetailsService userDetailsService(UserRepository userRepository) {
		return username -> userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + username));
	}
}

