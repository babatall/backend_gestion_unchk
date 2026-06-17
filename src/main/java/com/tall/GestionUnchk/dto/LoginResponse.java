package com.tall.GestionUnchk.dto;

import lombok.*;

/**
 * DTO pour la réponse d'authentification
 * Contient le token JWT
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    
    private String accessToken;
    @Builder.Default
    private String tokenType = "Bearer";
    private Long expiresIn;
    private UserDTO user;
}
