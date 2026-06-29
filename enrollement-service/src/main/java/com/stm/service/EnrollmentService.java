package com.stm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.stm.client.CourseClient;
import com.stm.client.StudentClient;
import com.stm.dto.Course;
import com.stm.entity.Enrollment;
import com.stm.exception.CourseNotFoundException;
import com.stm.exception.DuplicateEnrollmentException;
import com.stm.exception.EnrollmentNotFoundException;
import com.stm.exception.StudentNotFoundException;
import com.stm.repository.EnrollmentRepository;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentClient studentClient;
    private final CourseClient courseClient;

    //Assign Course
    public Enrollment assignCourse(
            Long studentId,
            Long courseId,
            String token) {

        try {
            studentClient.getStudent(studentId, token);
        } catch (FeignException.NotFound e) {
            throw new StudentNotFoundException(
                    "Student not found with ID: " + studentId
            );
        }

        Course course;

        try {
            course = courseClient.getCourse(courseId, token);
        } catch (FeignException.NotFound e) {
            throw new CourseNotFoundException(
                    "Course not found with ID: " + courseId
            );
        }

        enrollmentRepository
                .findByStudentIdAndCourseId(studentId, courseId)
                .ifPresent(enrollment -> {
                    throw new DuplicateEnrollmentException(
                            "Student already enrolled in this course"
                    );
                });

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(studentId);
        enrollment.setCourseId(courseId);

        enrollment.setCourseName(course.getCourseName());
        enrollment.setCourseDescription(course.getDescription());
        enrollment.setCourseFee(course.getCourseFees());

        return enrollmentRepository.save(enrollment);
    }
    // Get all courses for student
    public List<Enrollment> getStudentCourses(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    // Get all students by course
    public List<Enrollment> getStudentsByCourse(Long courseId) {

        List<Enrollment> enrollments =
                enrollmentRepository.findByCourseId(courseId);

        if (enrollments.isEmpty()) {
            throw new EnrollmentNotFoundException(
                    "No students found for Course ID: " + courseId
            );
        }

        return enrollments;
    }

    // Leave Course
    public void leaveCourse(Long studentId, Long courseId) {

        Enrollment enrollment = enrollmentRepository
                .findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() ->
                        new EnrollmentNotFoundException(
                                "Enrollment not found for Student ID: "
                                        + studentId +
                                        " and Course ID: " + courseId
                        )
                );

        enrollmentRepository.delete(enrollment);
    }
}