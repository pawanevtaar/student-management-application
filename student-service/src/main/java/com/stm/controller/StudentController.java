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

import com.stm.entity.Address;
import com.stm.entity.Student;
import com.stm.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@SecurityRequirement(name = "bearerAuth")
//@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    //private final StudentRepository studentRepository;
	/*
	 * @PostMapping("/login") public ResponseEntity<String> login(@RequestBody
	 * LoginRequestDto loginRequestDto) { return
	 * ResponseEntity.ok(studentService.login(loginRequestDto)); }
	 */
    @PostMapping()
    @Operation(summary = "Add New Student")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Student> addStudent(
            @RequestBody Student student) {
        return ResponseEntity.ok(
                studentService.addStudent(student));
    }

    @Operation(summary = "Update Student Profile")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    public ResponseEntity<Student> updateStudent(
            @PathVariable Long id,
            @RequestBody Student student) {
        return ResponseEntity.ok(
                studentService.updateProfile(id, student));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Student By ID")
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    public ResponseEntity<Student> getStudent(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                studentService.getById(id));
    }

    @GetMapping("/code/{studentCode}")
    @Operation(summary = "Get Student By Student Code")
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    public ResponseEntity<Student> getStudentByCode(
            @PathVariable String studentCode) {
        return ResponseEntity.ok(
                studentService.getByStudentCode(studentCode));
    }

    @PostMapping("/{id}/addresses")
    @Operation(summary = "Add Student Addresses")
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    public ResponseEntity<Student> addAddresses(
            @PathVariable Long id,
            @RequestBody List<Address> addresses) {
        return ResponseEntity.ok(
                studentService.addAddresses(id, addresses));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Student")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteStudent(
            @PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(
                "Student deleted successfully with ID:"+id);
    }
    
    @GetMapping("/validate")
    @Operation(summary = "Validate Student Credentials")
    public ResponseEntity<Student> validateStudent(
            @RequestParam String studentCode,
            @RequestParam String dateOfBirth) {

        Student student = studentService.validateStudent(
                studentCode,
                dateOfBirth
        );

        return ResponseEntity.ok(student);
    }
}