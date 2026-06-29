package com.stm.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stm.entity.Enrollment;
import com.stm.service.EnrollmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
//@RequestMapping("/enrollments")
@Tag(name = "Enrollment Controller", description = "Enrollment Management APIs")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    
    @Operation(summary = "Assign Course To Student")
    @PostMapping("/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Enrollment> assignCourse(
            @RequestParam Long studentId,
            @RequestParam Long courseId,
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                enrollmentService.assignCourse(
                        studentId,
                        courseId,
                        token));
    }
    @Operation(summary = "Get Courses By Student ID")
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    public ResponseEntity<List<Enrollment>> getStudentCourses(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(
                enrollmentService.getStudentCourses(studentId));
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get Students By Course ID")
    public ResponseEntity<List<Enrollment>> getStudentsByCourse(
            @PathVariable Long courseId) {
        return ResponseEntity.ok(
                enrollmentService.getStudentsByCourse(courseId));
    }

    @DeleteMapping("/leave")
    @Operation(summary = "Leave Course")
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    public ResponseEntity<String> leaveCourse(
            @RequestParam Long studentId,
            @RequestParam Long courseId) {
        enrollmentService.leaveCourse(studentId, courseId);
        return ResponseEntity.ok("Left course successfully from enrollment");
    }
}