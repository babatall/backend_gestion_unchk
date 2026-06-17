package com.tall.GestionUnchk.dto;

import com.tall.GestionUnchk.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO pour les requêtes d'inscription
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupRequest {
    
    @NotBlank(message = "Le nom d'utilisateur est requis")
    @Size(min = 3, max = 50, message = "Le nom d'utilisateur doit contenir entre 3 et 50 caractères")
    private String username;

    @NotBlank(message = "L'email est requis")
    @Email(message = "L'email doit être valide")
    private String email;

    @NotBlank(message = "Le mot de passe est requis")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String password;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Role role;
}
