package com.tall.GestionUnchk.service;

import com.tall.GestionUnchk.dto.UserCreateUpdateDTO;
import com.tall.GestionUnchk.dto.UserDTO;
import com.tall.GestionUnchk.entity.User;
import com.tall.GestionUnchk.exception.ResourceNotFoundException;
import com.tall.GestionUnchk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service de gestion des utilisateurs
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * Récupère tous les utilisateurs
     */
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Récupère un utilisateur par ID
     */
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + id));
        return convertToDTO(user);
    }
    
    /**
     * Récupère un utilisateur par email
     */
    @Transactional(readOnly = true)
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'email: " + email));
        return convertToDTO(user);
    }
    
    /**
     * Crée un nouvel utilisateur
     */
    public UserDTO createUser(UserCreateUpdateDTO userDTO) {
        User user = User.builder()
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getUsername())) // Mot de passe temporaire
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .phoneNumber(userDTO.getPhoneNumber())
                .profilePicture(userDTO.getProfilePicture())
                .isActive(userDTO.getIsActive() != null ? userDTO.getIsActive() : true)
                .isEnabled(userDTO.getIsEnabled() != null ? userDTO.getIsEnabled() : true)
                .build();
        
        User savedUser = userRepository.save(user);
        log.info("Utilisateur créé: {}", savedUser.getEmail());
        
        return convertToDTO(savedUser);
    }
    
    /**
     * Met à jour un utilisateur
     */
    public UserDTO updateUser(Long id, UserCreateUpdateDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + id));
        
        if (userDTO.getUsername() != null) {
            user.setUsername(userDTO.getUsername());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getFirstName() != null) {
            user.setFirstName(userDTO.getFirstName());
        }
        if (userDTO.getLastName() != null) {
            user.setLastName(userDTO.getLastName());
        }
        if (userDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(userDTO.getPhoneNumber());
        }
        if (userDTO.getProfilePicture() != null) {
            user.setProfilePicture(userDTO.getProfilePicture());
        }
        if (userDTO.getIsActive() != null) {
            user.setIsActive(userDTO.getIsActive());
        }
        if (userDTO.getIsEnabled() != null) {
            user.setIsEnabled(userDTO.getIsEnabled());
        }
        
        User updatedUser = userRepository.save(user);
        log.info("Utilisateur mis à jour: {}", updatedUser.getEmail());
        
        return convertToDTO(updatedUser);
    }
    
    /**
     * Supprime un utilisateur
     */
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + id));
        
        userRepository.delete(user);
        log.info("Utilisateur supprimé: {}", user.getEmail());
    }
    
    /**
     * Convertir une entité User en UserDTO
     */
    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .profilePicture(user.getProfilePicture())
                .isActive(user.getIsActive())
                .isEnabled(user.getIsEnabled())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .lastLogin(user.getLastLogin())
                .build();
    }
}
