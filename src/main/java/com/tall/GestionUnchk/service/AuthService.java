package com.tall.GestionUnchk.service;

import com.tall.GestionUnchk.dto.LoginRequest;
import com.tall.GestionUnchk.dto.LoginResponse;
import com.tall.GestionUnchk.dto.SignupRequest;
import com.tall.GestionUnchk.entity.User;
import com.tall.GestionUnchk.entity.RoleEntity;
import com.tall.GestionUnchk.enums.Role;
import com.tall.GestionUnchk.exception.ResourceAlreadyExistsException;
import com.tall.GestionUnchk.exception.UnauthorizedException;
import com.tall.GestionUnchk.repository.UserRepository;
import com.tall.GestionUnchk.repository.RoleRepository;
import com.tall.GestionUnchk.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tall.GestionUnchk.dto.UserDTO;
import com.tall.GestionUnchk.dto.RoleDTO;
import com.tall.GestionUnchk.dto.PermissionDTO;

import java.util.Set;
import java.util.stream.Collectors;

import java.time.LocalDateTime;

/**
 * Service d'authentification
 * Responsabilités:
 * - Authentification des utilisateurs
 * - Enregistrement des nouveaux utilisateurs
 * - Génération de tokens JWT
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * Authentifie un utilisateur et génère un token JWT
     */
    public LoginResponse login(LoginRequest loginRequest) {

        log.info("Tentative de connexion pour: {}", loginRequest.getEmail());

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() ->
                        new UnauthorizedException("Email ou mot de passe invalide"));

        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            String token = jwtService.generateToken(authentication);
            long expiresIn = jwtService.getExpirationTime();

            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);

            Set<RoleDTO> roles = user.getRoles()
                    .stream()
                    .map(role -> RoleDTO.builder()
                            .id(role.getId())
                            .name(role.getName())
                            .description(role.getDescription())
                            .permissions(
                                    role.getPermissions()
                                            .stream()
                                            .map(permission -> PermissionDTO.builder()
                                                    .id(permission.getId())
                                                    .name(permission.getName())
                                                    .description(permission.getDescription())
                                                    .build())
                                            .collect(Collectors.toSet())
                            )
                            .build())
                    .collect(Collectors.toSet());

            UserDTO userDTO = UserDTO.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .phoneNumber(user.getPhoneNumber())
                    .profilePicture(user.getProfilePicture())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .lastLogin(user.getLastLogin())
                    .roles(roles)
                    .isActive(user.getIsActive())
                    .isEnabled(user.getIsEnabled())
                    .build();

            log.info("Connexion réussie pour: {}", user.getEmail());

            return LoginResponse.builder()
                    .accessToken(token)
                    .tokenType("Bearer")
                    .expiresIn(expiresIn)
                    .user(userDTO)
                    .build();

        } catch (Exception e) {

            log.error(
                    "Authentification échouée pour: {}",
                    loginRequest.getEmail()
            );

            throw new UnauthorizedException(
                    "Email ou mot de passe invalide"
            );
        }
    }
    
    /**
     * Enregistre un nouvel utilisateur
     */
    public void signup(SignupRequest signupRequest) {
        log.info("Tentative d'inscription pour: {}", signupRequest.getEmail());
        
        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new ResourceAlreadyExistsException("L'email " + signupRequest.getEmail() + " existe déjà");
        }
        
        // Vérifier si le username existe déjà
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new ResourceAlreadyExistsException("Le username " + signupRequest.getUsername() + " existe déjà");
        }
        
        // Créer le nouvel utilisateur
        User user = User.builder()
                .username(signupRequest.getUsername())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
                .phoneNumber(signupRequest.getPhoneNumber())
                .isActive(true)
                .isEnabled(true)
                .build();
        
        // Attribuer le rôle ETUDIANT par défaut
        Role roleToAssign =
        signupRequest.getRole() != null
                ? signupRequest.getRole()
                : Role.ETUDIANT;

                RoleEntity role = roleRepository.findByName(roleToAssign)
                        .orElseThrow(() -> new RuntimeException("Rôle non trouvé"));

                user.getRoles().add(role);
        
        // Sauvegarder l'utilisateur
        userRepository.save(user);
        
        log.info("Inscription réussie pour: {}", user.getEmail());
    }
}
