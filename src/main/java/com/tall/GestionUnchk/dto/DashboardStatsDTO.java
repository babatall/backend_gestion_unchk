package com.tall.GestionUnchk.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardStatsDTO {

    private Long students;

    private Long teachers;

    private Long courses;

    private Long enrollments;

    private Long grades;
}