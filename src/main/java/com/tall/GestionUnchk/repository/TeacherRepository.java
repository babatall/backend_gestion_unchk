package com.tall.GestionUnchk.repository;

import com.tall.GestionUnchk.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository pour l'entité Teacher
 */
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    
    Optional<Teacher> findByEmployeeNumber(String employeeNumber);
    
    Optional<Teacher> findByUserId(Long userId);
    
    boolean existsByEmployeeNumber(String employeeNumber);

    boolean existsByUserId(Long userId);
}
