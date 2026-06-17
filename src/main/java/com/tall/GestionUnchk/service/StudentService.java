package com.tall.GestionUnchk.service;

import com.tall.GestionUnchk.dto.CourseDTO;
import com.tall.GestionUnchk.dto.StudentCreateUpdateDTO;
import com.tall.GestionUnchk.dto.StudentDTO;
import com.tall.GestionUnchk.entity.Course;
import com.tall.GestionUnchk.entity.Enrollment;
import com.tall.GestionUnchk.entity.Student;
import com.tall.GestionUnchk.exception.ResourceAlreadyExistsException;
import com.tall.GestionUnchk.exception.ResourceNotFoundException;
import com.tall.GestionUnchk.repository.EnrollmentRepository;
import com.tall.GestionUnchk.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tall.GestionUnchk.entity.User;
import com.tall.GestionUnchk.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service de gestion des étudiants
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {
    
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    
    /**
     * Récupère tous les étudiants
     */
    @Transactional(readOnly = true)
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Récupère un étudiant par ID
     */
    @Transactional(readOnly = true)
    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant non trouvé avec l'ID: " + id));
        return convertToDTO(student);
    }
    
    /**
     * Récupère un étudiant par numéro d'étudiant
     */
    @Transactional(readOnly = true)
    public StudentDTO getStudentByStudentNumber(String studentNumber) {
        Student student = studentRepository.findByStudentNumber(studentNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant non trouvé avec le numéro: " + studentNumber));
        return convertToDTO(student);
    }
    
    /**
     * Crée un nouvel étudiant
     */
    public StudentDTO createStudent(StudentCreateUpdateDTO studentDTO) {

    log.info("DTO reçu : {}", studentDTO);
    log.info("UserId reçu : {}", studentDTO.getUserId());

    if (studentRepository.existsByStudentNumber(studentDTO.getStudentNumber())) {
        throw new ResourceAlreadyExistsException(
                "Le numéro d'étudiant "
                        + studentDTO.getStudentNumber()
                        + " existe déjà");
    }

    User user = userRepository.findById(studentDTO.getUserId())
            .orElseThrow(() ->
                    new ResourceNotFoundException("Utilisateur introuvable"));

    log.info("Utilisateur trouvé : {}", user.getId());

    if (studentRepository.existsByUserId(studentDTO.getUserId())) {
        throw new ResourceAlreadyExistsException(
                "Cet utilisateur est déjà associé à un étudiant");
    }

    Student student = Student.builder()
            .studentNumber(studentDTO.getStudentNumber())
            .dateOfBirth(studentDTO.getDateOfBirth())
            .gender(studentDTO.getGender())
            .address(studentDTO.getAddress())
            .city(studentDTO.getCity())
            .postalCode(studentDTO.getPostalCode())
            .country(studentDTO.getCountry())
            .program(studentDTO.getProgram())
            .enrollmentYear(studentDTO.getEnrollmentYear())
            .gpa(studentDTO.getGpa())
            .isActive(
                    studentDTO.getIsActive() != null
                            ? studentDTO.getIsActive()
                            : true
            )
            .user(user)
            .build();

    log.info("User associé : {}", student.getUser());

    Student savedStudent = studentRepository.save(student);

    log.info(
            "Étudiant créé : {} - userId={}",
            savedStudent.getStudentNumber(),
            savedStudent.getUser() != null
                    ? savedStudent.getUser().getId()
                    : null
    );

    return convertToDTO(savedStudent);
}
        
            
                
    
    /**
     * Met à jour un étudiant
     */
    public StudentDTO updateStudent(
        Long id,
        StudentCreateUpdateDTO studentDTO
) {

    Student student = studentRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Étudiant non trouvé avec l'ID: " + id
                    )
            );

    if (studentDTO.getDateOfBirth() != null) {
        student.setDateOfBirth(studentDTO.getDateOfBirth());
    }

    if (studentDTO.getGender() != null) {
        student.setGender(studentDTO.getGender());
    }

    if (studentDTO.getAddress() != null) {
        student.setAddress(studentDTO.getAddress());
    }

    if (studentDTO.getCity() != null) {
        student.setCity(studentDTO.getCity());
    }

    if (studentDTO.getPostalCode() != null) {
        student.setPostalCode(studentDTO.getPostalCode());
    }

    if (studentDTO.getCountry() != null) {
        student.setCountry(studentDTO.getCountry());
    }

    if (studentDTO.getProgram() != null) {
        student.setProgram(studentDTO.getProgram());
    }

    if (studentDTO.getEnrollmentYear() != null) {
        student.setEnrollmentYear(studentDTO.getEnrollmentYear());
    }

    if (studentDTO.getGpa() != null) {
        student.setGpa(studentDTO.getGpa());
    }

    if (studentDTO.getIsActive() != null) {
        student.setIsActive(studentDTO.getIsActive());
    }

    if (studentDTO.getUserId() != null) {

        User user = userRepository.findById(studentDTO.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Utilisateur non trouvé avec l'ID : "
                                        + studentDTO.getUserId()
                        )
                );

        student.setUser(user);
    }

    Student updatedStudent = studentRepository.save(student);

    log.info(
            "Étudiant mis à jour: {} - userId={}",
            updatedStudent.getStudentNumber(),
            updatedStudent.getUser() != null
                    ? updatedStudent.getUser().getId()
                    : null
    );

    return convertToDTO(updatedStudent);
}
    
    /**
     * Supprime un étudiant
     */
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant non trouvé avec l'ID: " + id));
        
        studentRepository.delete(student);
        log.info("Étudiant supprimé: {}", student.getStudentNumber());
    }
    
    /**
     * Convertir une entité Student en StudentDTO
     */
    private StudentDTO convertToDTO(Student student) {
        return StudentDTO.builder()
                .id(student.getId())
                .studentNumber(student.getStudentNumber())
                .dateOfBirth(student.getDateOfBirth())
                .gender(student.getGender())
                .address(student.getAddress())
                .city(student.getCity())
                .postalCode(student.getPostalCode())
                .country(student.getCountry())
                .program(student.getProgram())
                .enrollmentYear(student.getEnrollmentYear())
                .gpa(student.getGpa())
                .isActive(student.getIsActive())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .userId(student.getUser() != null ? student.getUser().getId() : null)
                .build();
    }

    @Transactional(readOnly = true)
    public List<CourseDTO> getStudentCourses(Long studentId) {

        List<Enrollment> enrollments =
                enrollmentRepository.findByStudentId(studentId);

        return enrollments.stream()
                .map(enrollment -> {

                    Course course = enrollment.getCourse();

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
                            .teacherId(course.getTeacher().getId())
                            .build();
                })
                .toList();
    }
}
