package com.tall.GestionUnchk.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO pour les enseignants
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherDTO {
    
    private Long id;
    private String employeeNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private String department;
    private String specialty;
    private String academicTitle;
    private LocalDate hireDate;
    
    @JsonProperty("isActive")
    private Boolean isActive;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private Long userId;
    private Set<Long> courseIds;
}
