package com.tall.GestionUnchk.dto;

import com.tall.GestionUnchk.enums.Permission;
import lombok.*;

/**
 * DTO pour les permissions
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PermissionDTO {
    
    private Long id;
    private Permission name;
    private String description;
}
