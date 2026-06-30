package com.stm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.stm.client.CourseClient;
import com.stm.client.StudentClient;
import com.stm.dto.Course;
import com.stm.dto.Student;
import com.stm.entity.Enrollment;
import com.stm.exception.DuplicateEnrollmentException;
import com.stm.exception.EnrollmentNotFoundException;
import com.stm.repository.EnrollmentRepository;
import com.stm.service.EnrollmentService;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private StudentClient studentClient;

    @Mock
    private CourseClient courseClient;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private Enrollment enrollment;
    private Student student;
    private Course course;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setName("Pawan");

        course = new Course();
        course.setId(101L);
        course.setCourseName("Java");

        enrollment = new Enrollment();
        enrollment.setId(1L);
        enrollment.setStudentId(1L);
        enrollment.setCourseId(101L);
    }

    @Test
    void testAssignCourse_Success() {
        String token = "Bearer sample-token";

        when(studentClient.getStudent(1L, token)).thenReturn(student);
        when(courseClient.getCourse(101L, token)).thenReturn(course);
        when(enrollmentRepository.findByStudentIdAndCourseId(1L, 101L))
                .thenReturn(Optional.empty());
        when(enrollmentRepository.save(any(Enrollment.class)))
                .thenReturn(enrollment);

        Enrollment result = enrollmentService.assignCourse(1L, 101L, token);

        assertNotNull(result);
        assertEquals(1L, result.getStudentId());
        assertEquals(101L, result.getCourseId());

        verify(studentClient).getStudent(1L, token);
        verify(courseClient).getCourse(101L, token);
        verify(enrollmentRepository).save(any(Enrollment.class));
    }

    @Test
    void testAssignCourse_DuplicateEnrollment() {
        String token = "Bearer sample-token";

        when(studentClient.getStudent(1L, token)).thenReturn(student);
        when(courseClient.getCourse(101L, token)).thenReturn(course);
        when(enrollmentRepository.findByStudentIdAndCourseId(1L, 101L))
                .thenReturn(Optional.of(enrollment));

        assertThrows(DuplicateEnrollmentException.class, () ->
                enrollmentService.assignCourse(1L, 101L, token));

        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void testGetStudentCourses() {
        List<Enrollment> enrollments =
                Arrays.asList(enrollment);

        when(enrollmentRepository.findByStudentId(1L))
                .thenReturn(enrollments);

        List<Enrollment> result =
                enrollmentService.getStudentCourses(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getStudentId());
    }

    @Test
    void testGetStudentsByCourse() {
        List<Enrollment> enrollments =
                Arrays.asList(enrollment);

        when(enrollmentRepository.findByCourseId(101L))
                .thenReturn(enrollments);

        List<Enrollment> result =
                enrollmentService.getStudentsByCourse(101L);

        assertEquals(1, result.size());
        assertEquals(101L, result.get(0).getCourseId());
    }

    @Test
    void testLeaveCourse() {
        when(enrollmentRepository.findByStudentIdAndCourseId(1L, 101L))
                .thenReturn(Optional.of(enrollment));

        enrollmentService.leaveCourse(1L, 101L);

        verify(enrollmentRepository).delete(enrollment);
    }

    
}