package com.tall.GestionUnchk.dto;

import lombok.*;

/**
 * DTO pour créer/modifier les notes
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GradeCreateUpdateDTO {
    
    private Double score;
    private String letterGrade;
    private String comments;
    private Long studentId;
    private Long courseId;
    private Long teacherId;
}
