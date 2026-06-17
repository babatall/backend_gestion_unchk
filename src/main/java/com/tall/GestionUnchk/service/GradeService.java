package com.tall.GestionUnchk.service;

import com.tall.GestionUnchk.dto.GradeCreateUpdateDTO;
import com.tall.GestionUnchk.dto.GradeDTO;
import com.tall.GestionUnchk.entity.Course;
import com.tall.GestionUnchk.entity.Grade;
import com.tall.GestionUnchk.entity.Student;
import com.tall.GestionUnchk.entity.Teacher;
import com.tall.GestionUnchk.exception.ResourceNotFoundException;
import com.tall.GestionUnchk.repository.CourseRepository;
import com.tall.GestionUnchk.repository.GradeRepository;
import com.tall.GestionUnchk.repository.StudentRepository;
import com.tall.GestionUnchk.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service de gestion des notes (Grade)
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class GradeService {
    
    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    
    @Transactional(readOnly = true)
    public List<GradeDTO> getAllGrades() {
        return gradeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public GradeDTO getGradeById(Long id) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note non trouvée avec l'ID: " + id));
        return convertToDTO(grade);
    }
    
    public GradeDTO createGrade(GradeCreateUpdateDTO gradeDTO) {
        Student student = studentRepository.findById(gradeDTO.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant non trouvé"));
        
        Course course = courseRepository.findById(gradeDTO.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé"));
        
        Teacher teacher = teacherRepository.findById(gradeDTO.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Enseignant non trouvé"));
        
        Grade grade = Grade.builder()
                .score(gradeDTO.getScore())
                .letterGrade(gradeDTO.getLetterGrade())
                .comments(gradeDTO.getComments())
                .student(student)
                .course(course)
                .teacher(teacher)
                .build();
        
        Grade savedGrade = gradeRepository.save(grade);
        log.info("Note créée: Étudiant {} -> Cours {}, Score: {}", 
                student.getStudentNumber(), course.getCourseCode(), gradeDTO.getScore());
        
        return convertToDTO(savedGrade);
    }
    
    public GradeDTO updateGrade(Long id, GradeCreateUpdateDTO gradeDTO) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note non trouvée avec l'ID: " + id));
        
        if (gradeDTO.getScore() != null) {
            grade.setScore(gradeDTO.getScore());
        }
        if (gradeDTO.getLetterGrade() != null) {
            grade.setLetterGrade(gradeDTO.getLetterGrade());
        }
        if (gradeDTO.getComments() != null) {
            grade.setComments(gradeDTO.getComments());
        }
        
        Grade updatedGrade = gradeRepository.save(grade);
        log.info("Note mise à jour: ID {}, Nouveau score: {}", id, gradeDTO.getScore());
        
        return convertToDTO(updatedGrade);
    }
    
    public void deleteGrade(Long id) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note non trouvée avec l'ID: " + id));
        
        gradeRepository.delete(grade);
        log.info("Note supprimée: ID {}", id);
    }
    
    private GradeDTO convertToDTO(Grade grade) {
        return GradeDTO.builder()
                .id(grade.getId())
                .score(grade.getScore())
                .letterGrade(grade.getLetterGrade())
                .comments(grade.getComments())
                .gradingDate(grade.getGradingDate())
                .createdAt(grade.getCreatedAt())
                .updatedAt(grade.getUpdatedAt())
                .studentId(grade.getStudent().getId())
                .studentNumber(grade.getStudent().getStudentNumber())
                .courseId(grade.getCourse().getId())
                .courseCode(grade.getCourse().getCourseCode())
                .teacherId(grade.getTeacher().getId())
                .teacherName(grade.getTeacher().getUser() != null ? 
                        grade.getTeacher().getUser().getFirstName() + " " + grade.getTeacher().getUser().getLastName() : 
                        "")
                .build();
    }
}
