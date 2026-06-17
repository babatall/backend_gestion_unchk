package com.tall.GestionUnchk.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO pour les réponses User
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profilePicture;
    
    @JsonProperty("isActive")
    private Boolean isActive;
    
    @JsonProperty("isEnabled")
    private Boolean isEnabled;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLogin;
    
    private Set<RoleDTO> roles;
}
