package com.tall.GestionUnchk.dto;

import lombok.*;

import java.time.LocalDate;

/**
 * DTO pour créer/modifier les enseignants
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherCreateUpdateDTO {
    
    private String employeeNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private String department;
    private String specialty;
    private String academicTitle;
    private LocalDate hireDate;
    private Boolean isActive;
    private Long userId; // ID de l'utilisateur associé
}
