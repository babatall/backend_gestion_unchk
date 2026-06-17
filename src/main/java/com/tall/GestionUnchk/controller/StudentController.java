package com.tall.GestionUnchk.controller;

import com.tall.GestionUnchk.dto.CourseDTO;
import com.tall.GestionUnchk.dto.StudentCreateUpdateDTO;
import com.tall.GestionUnchk.dto.StudentDTO;
import com.tall.GestionUnchk.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur de gestion des étudiants
 */
@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@Tag(name = "Étudiants", description = "Gestion des étudiants")
@SecurityRequirement(name = "bearer-jwt")
public class StudentController {
    
    private final StudentService studentService;
    
    @GetMapping
    @Operation(summary = "Lister tous les étudiants")
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        List<StudentDTO> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un étudiant par ID")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        StudentDTO student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }
    
    @GetMapping("/number/{studentNumber}")
    @Operation(summary = "Obtenir un étudiant par numéro")
    public ResponseEntity<StudentDTO> getStudentByNumber(@PathVariable String studentNumber) {
        StudentDTO student = studentService.getStudentByStudentNumber(studentNumber);
        return ResponseEntity.ok(student);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Créer un étudiant")
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody StudentCreateUpdateDTO studentDTO) {
        StudentDTO createdStudent = studentService.createStudent(studentDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdStudent);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Mettre à jour un étudiant")
    public ResponseEntity<StudentDTO> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentCreateUpdateDTO studentDTO) {
        StudentDTO updatedStudent = studentService.updateStudent(id, studentDTO);
        return ResponseEntity.ok(updatedStudent);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Supprimer un étudiant")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<List<CourseDTO>> getStudentCourses(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                studentService.getStudentCourses(id)
        );
    }
}
