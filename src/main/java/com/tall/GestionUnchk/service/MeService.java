package com.tall.GestionUnchk.service;

import com.tall.GestionUnchk.dto.CourseDTO;
import com.tall.GestionUnchk.dto.GradeCreateUpdateDTO;
import com.tall.GestionUnchk.dto.GradeDTO;
import com.tall.GestionUnchk.dto.StudentDTO;
import com.tall.GestionUnchk.entity.*;
import com.tall.GestionUnchk.exception.ResourceNotFoundException;
import com.tall.GestionUnchk.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MeService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final GradeRepository gradeRepository;

    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;

    public List<CourseDTO> getMyCourses(
            Authentication authentication
    ) {

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Utilisateur introuvable")
                );

        Student student = studentRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Étudiant introuvable")
                );

        return enrollmentRepository
                .findByStudentId(student.getId())
                .stream()
                .map(this::convertCourseToDTO)
                .toList();
    }



    private CourseDTO convertCourseToDTO(
            Enrollment enrollment
    ) {

        return CourseDTO.builder()
                .id(enrollment.getCourse().getId())
                .courseCode(enrollment.getCourse().getCourseCode())
                .title(enrollment.getCourse().getTitle())
                .description(enrollment.getCourse().getDescription())
                .credits(enrollment.getCourse().getCredits())
                .program(enrollment.getCourse().getProgram())
                .semester(enrollment.getCourse().getSemester())
                .maxStudents(enrollment.getCourse().getMaxStudents())
                .teacherId(
                        enrollment.getCourse()
                                .getTeacher()
                                .getId()
                )
                .build();
    }





    public List<GradeDTO> getMyGrades(
            Authentication authentication
    ) {

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Utilisateur introuvable"
                        )
                );

        Student student = studentRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Étudiant introuvable"
                        )
                );

        return gradeRepository
                .findByStudentId(student.getId())
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    private GradeDTO convertToDTO(
            Grade grade
    ) {

        return GradeDTO.builder()
                .id(grade.getId())
                .score(grade.getScore())
                .letterGrade(grade.getLetterGrade())
                .comments(grade.getComments())
                .gradingDate(grade.getGradingDate())
                .createdAt(grade.getCreatedAt())
                .updatedAt(grade.getUpdatedAt())

                .studentId(grade.getStudent().getId())
                .studentNumber(
                        grade.getStudent().getStudentNumber()
                )

                .courseId(grade.getCourse().getId())
                .courseCode(
                        grade.getCourse().getCourseCode()
                )

                .teacherId(grade.getTeacher().getId())

                .teacherName(
                        grade.getTeacher()
                                .getUser()
                                .getFirstName()
                                + " "
                                + grade.getTeacher()
                                .getUser()
                                .getLastName()
                )

                .build();
    }



    public List<CourseDTO> getTeacherCourses() {

        String username =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Utilisateur introuvable"));

        Teacher teacher = teacherRepository
                .findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Enseignant introuvable"));

        return courseRepository
                .findByTeacherId(teacher.getId())
                .stream()
                .map(course -> CourseDTO.builder()
                        .id(course.getId())
                        .courseCode(course.getCourseCode())
                        .title(course.getTitle())
                        .description(course.getDescription())
                        .credits(course.getCredits())
                        .program(course.getProgram())
                        .semester(course.getSemester())
                        .maxStudents(course.getMaxStudents())
                        .teacherId(teacher.getId())
                        .isActive(course.getIsActive())
                        .build())
                .toList();
    }

    public List<StudentDTO> getTeacherStudents() {

        String username =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Utilisateur introuvable"));

        Teacher teacher = teacherRepository
                .findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Enseignant introuvable"));

        List<Course> courses =
                courseRepository.findByTeacherId(
                        teacher.getId()
                );

        Set<Student> students = new HashSet<>();

        for (Course course : courses) {

            List<Enrollment> enrollments =
                    enrollmentRepository.findByCourseId(
                            course.getId()
                    );

            enrollments.forEach(
                    e -> students.add(
                            e.getStudent()
                    )
            );
        }

        return students.stream()
                .map(student -> StudentDTO.builder()
                        .id(student.getId())
                        .studentNumber(student.getStudentNumber())
                        .program(student.getProgram())
                        .userId(
                                student.getUser() != null
                                        ? student.getUser().getId()
                                        : null
                        )
                        .build())
                .toList();
    }



    public List<GradeDTO> getTeacherGrades() {

        String username =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("Utilisateur introuvable"));

        Teacher teacher = teacherRepository
                .findByUserId(user.getId())
                .orElseThrow(() ->
                        new RuntimeException("Enseignant introuvable"));

        return gradeRepository
                .findByTeacherId(teacher.getId())
                .stream()
                .map(this::convertGradeToDto)
                .toList();
    }

    private GradeDTO convertGradeToDto(Grade grade) {

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
                .teacherName(
                        grade.getTeacher().getUser().getFirstName()
                                + " "
                                + grade.getTeacher().getUser().getLastName()
                )

                .build();
    }

    public GradeDTO createTeacherGrade(
            GradeCreateUpdateDTO dto
    ) {

        String username =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("Utilisateur introuvable"));

        Teacher teacher = teacherRepository
                .findByUserId(user.getId())
                .orElseThrow(() ->
                        new RuntimeException("Enseignant introuvable"));

        Student student = studentRepository
                .findById(dto.getStudentId())
                .orElseThrow(() ->
                        new RuntimeException("Étudiant introuvable"));

        Course course = courseRepository
                .findById(dto.getCourseId())
                .orElseThrow(() ->
                        new RuntimeException("Cours introuvable"));

        // Vérification que le cours appartient bien au prof connecté
        if (!course.getTeacher()
                .getId()
                .equals(teacher.getId())) {

            throw new RuntimeException(
                    "Vous ne pouvez noter que vos propres cours"
            );
        }

        // Création ou mise à jour
        Grade grade = gradeRepository
                .findByStudentIdAndCourseId(
                        dto.getStudentId(),
                        dto.getCourseId()
                )
                .orElse(new Grade());

        grade.setStudent(student);
        grade.setCourse(course);
        grade.setTeacher(teacher);

        grade.setScore(dto.getScore());
        grade.setLetterGrade(dto.getLetterGrade());
        grade.setComments(dto.getComments());

        grade = gradeRepository.save(grade);

        return convertGradeToDto(grade);
    }
}