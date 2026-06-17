package com.tall.GestionUnchk.dto;

import com.tall.GestionUnchk.enums.Role;
import lombok.*;

import java.util.Set;

/**
 * DTO pour les rôles
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDTO {
    
    private Long id;
    private Role name;
    private String description;
    private Set<PermissionDTO> permissions;
}
