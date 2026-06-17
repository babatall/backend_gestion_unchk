package com.tall.GestionUnchk.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO pour les cours
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseDTO {
    
    private Long id;
    private String courseCode;
    private String title;
    private String description;
    private Integer credits;
    private String program;
    private Integer semester;
    private Integer maxStudents;
    
    @JsonProperty("isActive")
    private Boolean isActive;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private Long teacherId;
    private String teacherName;
    private Set<Long> enrollmentIds;
}
