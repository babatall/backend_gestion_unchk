package com.tall.GestionUnchk.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO pour créer/modifier les étudiants
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentCreateUpdateDTO {
    
    private String studentNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private String city;
    private String postalCode;
    private String country;
    private String program;
    private Integer enrollmentYear;
    private Double gpa;
    private Boolean isActive;
    private Long userId; // ID de l'utilisateur associé
}
