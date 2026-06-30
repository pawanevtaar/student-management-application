package com.stm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
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

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Basic;
import com.stm.entity.Course;
import com.stm.exception.CourseNotFoundException;
import com.stm.repository.CourseRepository;
import com.stm.service.CourseService;

import net.bytebuddy.asm.Advice.AssignReturned.ToAllArguments;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    private Course course;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setId(1L);
        course.setCourseName("Java Full Stack");
        course.setDescription("Spring Boot + React");
        course.setCourseType("Basic To Advance");
        course.setDuration(6);
    }

    @Test
    void testCreateCourse() {
        when(courseRepository.save(course)).thenReturn(course);

        Course savedCourse = courseService.addCourse(course);

        assertNotNull(savedCourse);
        assertEquals("Java Full Stack", savedCourse.getCourseName());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testGetCourseById() {
        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        Course result = courseService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Java Full Stack", result.getCourseName());
    }

    @Test
    void testGetCourseById_NotFound() {
        when(courseRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class,
                () -> courseService.getById(1L));
    }

    @Test
    void testGetAllCourses() {
        List<Course> courses = Arrays.asList(course);

        when(courseRepository.findAll()).thenReturn(courses);

        List<Course> result = courseService.getAll();

        assertEquals(1, result.size());
        assertEquals("Java Full Stack", result.get(0).getCourseName());
    }

    @Test
    void testUpdateCourse() {
        Course updatedCourse = new Course();
        updatedCourse.setCourseName("Advanced Java");
        updatedCourse.setDescription("Spring Boot Microservices");
        //.setCourseType(35000.0);

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        when(courseRepository.save(any(Course.class)))
                .thenReturn(course);

        Course result = courseService.updateCourse(1L, updatedCourse);

        assertNotNull(result);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testDeleteCourse() {
        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        doNothing().when(courseRepository).delete(course);

        courseService.deleteCourse(1L);

        verify(courseRepository, times(1)).delete(course);
    }

    @Test
    void testDeleteCourse_NotFound() {
        when(courseRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class,
                () -> courseService.deleteCourse(1L));
    }
}