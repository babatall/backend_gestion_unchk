package com.tall.GestionUnchk.controller;

import com.tall.GestionUnchk.dto.GradeCreateUpdateDTO;
import com.tall.GestionUnchk.dto.GradeDTO;
import com.tall.GestionUnchk.service.GradeService;
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
 * Contrôleur de gestion des notes (Grade)
 */
@RestController
@RequestMapping("/api/v1/grades")
@RequiredArgsConstructor
@Tag(name = "Notes", description = "Gestion des notes/évaluations")
@SecurityRequirement(name = "bearer-jwt")
public class GradeController {
    
    private final GradeService gradeService;
    
    @GetMapping
    @Operation(summary = "Lister toutes les notes")
    public ResponseEntity<List<GradeDTO>> getAllGrades() {
        List<GradeDTO> grades = gradeService.getAllGrades();
        return ResponseEntity.ok(grades);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtenir une note par ID")
    public ResponseEntity<GradeDTO> getGradeById(@PathVariable Long id) {
        GradeDTO grade = gradeService.getGradeById(id);
        return ResponseEntity.ok(grade);
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ENSEIGNANT')")
    @Operation(summary = "Créer une note")
    public ResponseEntity<GradeDTO> createGrade(@Valid @RequestBody GradeCreateUpdateDTO gradeDTO) {
        GradeDTO createdGrade = gradeService.createGrade(gradeDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdGrade);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENSEIGNANT')")
    @Operation(summary = "Mettre à jour une note")
    public ResponseEntity<GradeDTO> updateGrade(
            @PathVariable Long id,
            @Valid @RequestBody GradeCreateUpdateDTO gradeDTO) {
        GradeDTO updatedGrade = gradeService.updateGrade(id, gradeDTO);
        return ResponseEntity.ok(updatedGrade);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Supprimer une note")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
        gradeService.deleteGrade(id);
        return ResponseEntity.noContent().build();
    }
}
