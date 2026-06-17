package com.tall.GestionUnchk.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO pour les inscriptions (Enrollment)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrollmentDTO {
    
    private Long id;
    private LocalDateTime enrollmentDate;
    
    @JsonProperty("isActive")
    private Boolean isActive;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private Long studentId;
    private String studentNumber;
    private Long courseId;
    private String courseCode;
}
