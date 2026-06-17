package com.tall.GestionUnchk.dto;

import lombok.*;

/**
 * DTO pour les requêtes de création/modification User
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateUpdateDTO {
    
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profilePicture;
    private Boolean isActive;
    private Boolean isEnabled;
}
