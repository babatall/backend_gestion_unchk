package com.tall.GestionUnchk.service;

import com.tall.GestionUnchk.dto.CourseCreateUpdateDTO;
import com.tall.GestionUnchk.dto.CourseDTO;
import com.tall.GestionUnchk.entity.Course;
import com.tall.GestionUnchk.entity.Teacher;
import com.tall.GestionUnchk.exception.ResourceAlreadyExistsException;
import com.tall.GestionUnchk.exception.ResourceNotFoundException;
import com.tall.GestionUnchk.repository.CourseRepository;
import com.tall.GestionUnchk.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service de gestion des cours
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {
    
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    
    @Transactional(readOnly = true)
    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public CourseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé avec l'ID: " + id));
        return convertToDTO(course);
    }
    
    public CourseDTO createCourse(CourseCreateUpdateDTO courseDTO) {
        if (courseRepository.existsByCourseCode(courseDTO.getCourseCode())) {
            throw new ResourceAlreadyExistsException("Le code de cours " + courseDTO.getCourseCode() + " existe déjà");
        }
        
        Teacher teacher = teacherRepository.findById(courseDTO.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Enseignant non trouvé"));
        
        Course course = Course.builder()
                .courseCode(courseDTO.getCourseCode())
                .title(courseDTO.getTitle())
                .description(courseDTO.getDescription())
                .credits(courseDTO.getCredits())
                .program(courseDTO.getProgram())
                .semester(courseDTO.getSemester())
                .maxStudents(courseDTO.getMaxStudents())
                .isActive(courseDTO.getIsActive() != null ? courseDTO.getIsActive() : true)
                .teacher(teacher)
                .build();
        
        Course savedCourse = courseRepository.save(course);
        log.info("Cours créé: {}", savedCourse.getCourseCode());
        
        return convertToDTO(savedCourse);
    }
    
    public CourseDTO updateCourse(Long id, CourseCreateUpdateDTO courseDTO) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé avec l'ID: " + id));
        
        if (courseDTO.getTitle() != null) {
            course.setTitle(courseDTO.getTitle());
        }
        if (courseDTO.getDescription() != null) {
            course.setDescription(courseDTO.getDescription());
        }
        if (courseDTO.getCredits() != null) {
            course.setCredits(courseDTO.getCredits());
        }
        if (courseDTO.getProgram() != null) {
            course.setProgram(courseDTO.getProgram());
        }
        if (courseDTO.getSemester() != null) {
            course.setSemester(courseDTO.getSemester());
        }
        if (courseDTO.getMaxStudents() != null) {
            course.setMaxStudents(courseDTO.getMaxStudents());
        }
        if (courseDTO.getIsActive() != null) {
            course.setIsActive(courseDTO.getIsActive());
        }
        if (courseDTO.getTeacherId() != null) {
            Teacher teacher = teacherRepository.findById(courseDTO.getTeacherId())
                    .orElseThrow(() -> new ResourceNotFoundException("Enseignant non trouvé"));
            course.setTeacher(teacher);
        }
        
        Course updatedCourse = courseRepository.save(course);
        log.info("Cours mis à jour: {}", updatedCourse.getCourseCode());
        
        return convertToDTO(updatedCourse);
    }
    
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé avec l'ID: " + id));
        
        courseRepository.delete(course);
        log.info("Cours supprimé: {}", course.getCourseCode());
    }
    
    private CourseDTO convertToDTO(Course course) {
        return CourseDTO.builder()
                .id(course.getId())
                .courseCode(course.getCourseCode())
                .title(course.getTitle())
                .description(course.getDescription())
                .credits(course.getCredits())
                .program(course.getProgram())
                .semester(course.getSemester())
                .maxStudents(course.getMaxStudents())
                .isActive(course.getIsActive())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .teacherId(course.getTeacher().getId())
                .teacherName(course.getTeacher().getUser() != null ? 
                        course.getTeacher().getUser().getFirstName() + " " + course.getTeacher().getUser().getLastName() : 
                        "")
                .build();
    }


}
