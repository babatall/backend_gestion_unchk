package com.tall.GestionUnchk.repository;

import com.tall.GestionUnchk.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité Course
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    Optional<Course> findByCourseCode(String courseCode);
    
    List<Course> findByTeacherId(Long teacherId);
    
    List<Course> findByProgram(String program);
    
    boolean existsByCourseCode(String courseCode);
}
