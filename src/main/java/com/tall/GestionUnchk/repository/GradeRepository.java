package com.tall.GestionUnchk.repository;

import com.tall.GestionUnchk.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité Grade
 */
@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    
    Optional<Grade> findByStudentIdAndCourseId(Long studentId, Long courseId);
    
    List<Grade> findByStudentId(Long studentId);
    
    List<Grade> findByCourseId(Long courseId);
    
    List<Grade> findByTeacherId(Long teacherId);


    
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
}
