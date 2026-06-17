package com.tall.GestionUnchk.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO pour les notes (Grade)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GradeDTO {
    
    private Long id;
    private Double score;
    private String letterGrade;
    private String comments;
    private LocalDateTime gradingDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private Long studentId;
    private String studentNumber;
    private Long courseId;
    private String courseCode;
    private Long teacherId;
    private String teacherName;
}
