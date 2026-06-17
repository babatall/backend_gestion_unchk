package com.tall.GestionUnchk.service;

import com.tall.GestionUnchk.dto.TeacherCreateUpdateDTO;
import com.tall.GestionUnchk.dto.TeacherDTO;
import com.tall.GestionUnchk.entity.Teacher;
import com.tall.GestionUnchk.entity.User;
import com.tall.GestionUnchk.exception.ResourceAlreadyExistsException;
import com.tall.GestionUnchk.exception.ResourceNotFoundException;
import com.tall.GestionUnchk.repository.TeacherRepository;
import com.tall.GestionUnchk.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service de gestion des enseignants
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TeacherService {
    
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    
    @Transactional(readOnly = true)
    public List<TeacherDTO> getAllTeachers() {
        return teacherRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public TeacherDTO getTeacherById(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enseignant non trouvé avec l'ID: " + id));
        return convertToDTO(teacher);
    }
    
   public TeacherDTO createTeacher(TeacherCreateUpdateDTO teacherDTO) {
    log.info("DTO reçu : {}", teacherDTO);
    log.info("UserId reçu : {}", teacherDTO.getUserId());

    if (teacherRepository.existsByEmployeeNumber(teacherDTO.getEmployeeNumber())) {
        throw new ResourceAlreadyExistsException(
                "Le numéro d'employé " + teacherDTO.getEmployeeNumber() + " existe déjà");
    }

    User user = userRepository.findById(teacherDTO.getUserId())
            .orElseThrow(() ->
                    new ResourceNotFoundException("Utilisateur introuvable"));
                    log.info("Utilisateur trouvé : {}", user.getId());

    if (teacherRepository.existsByUserId(teacherDTO.getUserId())) {
    throw new ResourceAlreadyExistsException(
            "Cet utilisateur est déjà associé à un enseignant");
}
    Teacher teacher = Teacher.builder()
            .employeeNumber(teacherDTO.getEmployeeNumber())
            .dateOfBirth(teacherDTO.getDateOfBirth())
            .gender(teacherDTO.getGender())
            .department(teacherDTO.getDepartment())
            .specialty(teacherDTO.getSpecialty())
            .academicTitle(teacherDTO.getAcademicTitle())
            .hireDate(teacherDTO.getHireDate())
            .isActive(
                    teacherDTO.getIsActive() != null
                            ? teacherDTO.getIsActive()
                            : true
            )
            .user(user)
            .build();
            log.info("User associé : {}", teacher.getUser());
    Teacher savedTeacher = teacherRepository.save(teacher);

    log.info("Enseignant créé: {}", savedTeacher.getEmployeeNumber());

    return convertToDTO(savedTeacher);
}
    
    public TeacherDTO updateTeacher(Long id, TeacherCreateUpdateDTO teacherDTO) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enseignant non trouvé avec l'ID: " + id));
        
        if (teacherDTO.getGender() != null) {
            teacher.setGender(teacherDTO.getGender());
        }
        if (teacherDTO.getDepartment() != null) {
            teacher.setDepartment(teacherDTO.getDepartment());
        }
        if (teacherDTO.getSpecialty() != null) {
            teacher.setSpecialty(teacherDTO.getSpecialty());
        }
        if (teacherDTO.getAcademicTitle() != null) {
            teacher.setAcademicTitle(teacherDTO.getAcademicTitle());
        }
        if (teacherDTO.getIsActive() != null) {
            teacher.setIsActive(teacherDTO.getIsActive());
        }
        
        Teacher updatedTeacher = teacherRepository.save(teacher);
        log.info("Enseignant mis à jour: {}", updatedTeacher.getEmployeeNumber());
        
        return convertToDTO(updatedTeacher);
    }
    
    public void deleteTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enseignant non trouvé avec l'ID: " + id));
        
        teacherRepository.delete(teacher);
        log.info("Enseignant supprimé: {}", teacher.getEmployeeNumber());
    }
    
    private TeacherDTO convertToDTO(Teacher teacher) {
        return TeacherDTO.builder()
                .id(teacher.getId())
                .employeeNumber(teacher.getEmployeeNumber())
                .dateOfBirth(teacher.getDateOfBirth())
                .gender(teacher.getGender())
                .department(teacher.getDepartment())
                .specialty(teacher.getSpecialty())
                .academicTitle(teacher.getAcademicTitle())
                .hireDate(teacher.getHireDate())
                .isActive(teacher.getIsActive())
                .createdAt(teacher.getCreatedAt())
                .updatedAt(teacher.getUpdatedAt())
                .userId(teacher.getUser() != null ? teacher.getUser().getId() : null)
                .build();
    }


}
