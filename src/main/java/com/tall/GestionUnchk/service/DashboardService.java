package com.tall.GestionUnchk.service;

import com.tall.GestionUnchk.dto.DashboardStatsDTO;
import com.tall.GestionUnchk.repository.*;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final StudentRepository studentRepository;

    private final TeacherRepository teacherRepository;

    private final CourseRepository courseRepository;

    private final EnrollmentRepository enrollmentRepository;

    private final GradeRepository gradeRepository;

    public DashboardStatsDTO getStats() {

        return DashboardStatsDTO.builder()
                .students(studentRepository.count())
                .teachers(teacherRepository.count())
                .courses(courseRepository.count())
                .enrollments(enrollmentRepository.count())
                .grades(gradeRepository.count())
                .build();
    }
}