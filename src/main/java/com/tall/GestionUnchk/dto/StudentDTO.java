package com.tall.GestionUnchk.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO pour les étudiants
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentDTO {
    
    private Long id;
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
    
    @JsonProperty("isActive")
    private Boolean isActive;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private Long userId;
    private Set<Long> enrollmentIds;
}
