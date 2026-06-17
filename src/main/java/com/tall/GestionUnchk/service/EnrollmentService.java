package com.tall.GestionUnchk.service;

import com.tall.GestionUnchk.dto.EnrollmentCreateUpdateDTO;
import com.tall.GestionUnchk.dto.EnrollmentDTO;
import com.tall.GestionUnchk.entity.Course;
import com.tall.GestionUnchk.entity.Enrollment;
import com.tall.GestionUnchk.entity.Student;
import com.tall.GestionUnchk.exception.ResourceAlreadyExistsException;
import com.tall.GestionUnchk.exception.ResourceNotFoundException;
import com.tall.GestionUnchk.repository.CourseRepository;
import com.tall.GestionUnchk.repository.EnrollmentRepository;
import com.tall.GestionUnchk.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service de gestion des inscriptions (Enrollment)
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentService {
    
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    
    @Transactional(readOnly = true)
    public List<EnrollmentDTO> getAllEnrollments() {
        return enrollmentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public EnrollmentDTO getEnrollmentById(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscription non trouvée avec l'ID: " + id));
        return convertToDTO(enrollment);
    }
    
    public EnrollmentDTO createEnrollment(EnrollmentCreateUpdateDTO enrollmentDTO) {
        Student student = studentRepository.findById(enrollmentDTO.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant non trouvé"));
        
        Course course = courseRepository.findById(enrollmentDTO.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé"));
        
        if (enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), course.getId())) {
            throw new ResourceAlreadyExistsException("L'étudiant est déjà inscrit à ce cours");
        }
        
        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .isActive(enrollmentDTO.getIsActive() != null ? enrollmentDTO.getIsActive() : true)
                .build();
        
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        log.info("Inscription créée: Étudiant {} -> Cours {}", student.getStudentNumber(), course.getCourseCode());
        
        return convertToDTO(savedEnrollment);
    }

    public EnrollmentDTO updateEnrollment(
            Long id,
            EnrollmentCreateUpdateDTO enrollmentDTO
    ) {

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Inscription introuvable avec l'ID : " + id
                        )
                );

        if (enrollmentDTO.getIsActive() != null) {
            enrollment.setIsActive(enrollmentDTO.getIsActive());
        }

        Enrollment updatedEnrollment =
                enrollmentRepository.save(enrollment);

        log.info(
                "Inscription mise à jour : {} -> {}",
                updatedEnrollment.getId(),
                updatedEnrollment.getIsActive()
        );

        return convertToDTO(updatedEnrollment);
    }
    
    public void deleteEnrollment(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscription non trouvée avec l'ID: " + id));
        
        enrollmentRepository.delete(enrollment);
        log.info("Inscription supprimée: ID {}", id);
    }
    
    private EnrollmentDTO convertToDTO(Enrollment enrollment) {
        return EnrollmentDTO.builder()
                .id(enrollment.getId())
                .enrollmentDate(enrollment.getEnrollmentDate())
                .isActive(enrollment.getIsActive())
                .createdAt(enrollment.getCreatedAt())
                .updatedAt(enrollment.getUpdatedAt())
                .studentId(enrollment.getStudent().getId())
                .studentNumber(enrollment.getStudent().getStudentNumber())
                .courseId(enrollment.getCourse().getId())
                .courseCode(enrollment.getCourse().getCourseCode())
                .build();
    }
}
