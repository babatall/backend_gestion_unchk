package com.tall.GestionUnchk.dto;

import lombok.*;

/**
 * DTO pour créer/modifier les cours
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseCreateUpdateDTO {
    
    private String courseCode;
    private String title;
    private String description;
    private Integer credits;
    private String program;
    private Integer semester;
    private Integer maxStudents;
    private Boolean isActive;
    private Long teacherId;
}
