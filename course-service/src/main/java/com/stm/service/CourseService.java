package com.stm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.stm.entity.Course;
import com.stm.exception.CourseNotFoundException;
import com.stm.repository.CourseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    // Add Course
    public Course addCourse(Course course) {
        return courseRepository.save(course);
    }

    // Update Course
    public Course updateCourse(Long id, Course updatedCourse) {

        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() ->
                        new CourseNotFoundException(
                                "Course not found with ID: " + id
                        )
                );

        existingCourse.setCourseName(updatedCourse.getCourseName());
        existingCourse.setDescription(updatedCourse.getDescription());
        existingCourse.setCourseType(updatedCourse.getCourseType());
        existingCourse.setDuration(updatedCourse.getDuration());
        existingCourse.setTopics(updatedCourse.getTopics());

        return courseRepository.save(existingCourse);
    }

    // Get Course By ID
    public Course getById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() ->
                        new CourseNotFoundException(
                                "Course not found with ID: " + id
                        )
                );
    }

    // Get All Courses
    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    // Search Courses
    public List<Course> search(String name) {

        List<Course> courses =
                courseRepository.findByCourseNameContainingIgnoreCase(name);

        if (courses.isEmpty()) {
            throw new CourseNotFoundException(
                    "No courses found with name: " + name
            );
        }

        return courses;
    }

    // Delete Course
    public void deleteCourse(Long id) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new CourseNotFoundException(
                                "Course not found with ID: " + id
                        )
                );

        courseRepository.delete(course);
    }
}