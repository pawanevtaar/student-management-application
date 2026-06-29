package com.stm.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stm.entity.Course;
import com.stm.service.CourseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
@Tag(name = "Course Controller", description = "Course Management APIs")
@RestController
//@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    @Operation(summary = "Add New Course")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Course> addCourse(
            @RequestBody Course course) {
        return ResponseEntity.ok(
                courseService.addCourse(course));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update Course")
    public ResponseEntity<Course> updateCourse(
            @PathVariable Long id,
            @RequestBody Course course) {
        return ResponseEntity.ok(
                courseService.updateCourse(id, course));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Course By ID")
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    public ResponseEntity<Course> getCourse(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                courseService.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get All Courses")
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(
                courseService.getAll());
    }

    @GetMapping("/search")
    @Operation(summary = "Search Course by Name")
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    public ResponseEntity<List<Course>> searchCourse(
            @RequestParam String name) {
        return ResponseEntity.ok(
                courseService.search(name));
    }

    @Operation(summary = "Delete Course")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCourse(
            @PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok(
                "Course deleted successfully with ID:"+id);
    }
}