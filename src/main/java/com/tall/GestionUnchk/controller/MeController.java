package com.tall.GestionUnchk.controller;

import com.tall.GestionUnchk.dto.CourseDTO;
import com.tall.GestionUnchk.dto.GradeCreateUpdateDTO;
import com.tall.GestionUnchk.dto.GradeDTO;
import com.tall.GestionUnchk.dto.StudentDTO;
import com.tall.GestionUnchk.service.MeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/me")
@RequiredArgsConstructor
public class MeController {

    private final MeService meService;



    @GetMapping("/teacher/grades")
    public ResponseEntity<List<GradeDTO>> getTeacherGrades() {

        return ResponseEntity.ok(
                meService.getTeacherGrades()
        );
    }

    @GetMapping("/teacher/courses")
    public ResponseEntity<List<CourseDTO>> getMyCourses() {
        return ResponseEntity.ok(
                meService.getTeacherCourses()
        );
    }

    @GetMapping("/teacher/students")
    public ResponseEntity<List<StudentDTO>> getMyStudents() {
        return ResponseEntity.ok(
                meService.getTeacherStudents()
        );
    }

    @PostMapping("/teacher/grades")
    public ResponseEntity<GradeDTO> createTeacherGrade(
            @RequestBody GradeCreateUpdateDTO dto
    ) {

        return ResponseEntity.ok(
                meService.createTeacherGrade(dto)
        );
    }
}