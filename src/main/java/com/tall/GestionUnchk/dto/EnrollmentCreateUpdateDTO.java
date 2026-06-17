package com.tall.GestionUnchk.dto;

import lombok.*;

/**
 * DTO pour créer/modifier les inscriptions
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrollmentCreateUpdateDTO {
    
    private Long studentId;
    private Long courseId;
    private Boolean isActive;
}
