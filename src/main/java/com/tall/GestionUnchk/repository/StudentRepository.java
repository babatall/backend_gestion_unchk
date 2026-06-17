package com.tall.GestionUnchk.repository;

import com.tall.GestionUnchk.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository pour l'entité Student
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    Optional<Student> findByStudentNumber(String studentNumber);
    boolean existsByUserId(Long userId);
    boolean existsByStudentNumber(String studentNumber);
    Optional<Student> findByUserId(Long userId);
    

}
