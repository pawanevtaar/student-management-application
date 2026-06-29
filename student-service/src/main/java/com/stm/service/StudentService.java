/*
 * package com.stm.service;
 * 
 * import java.util.List; import java.util.Random;
 * 
 * import org.springframework.stereotype.Service;
 * 
 * import com.stm.dto.LoginRequestDto; import com.stm.entity.Address; import
 * com.stm.entity.Student; import com.stm.filter.JwtUtil; import
 * com.stm.repository.StudentRepository;
 * 
 * 
 * import jakarta.transaction.Transactional; import
 * lombok.RequiredArgsConstructor;
 * 
 * @Service
 * 
 * @RequiredArgsConstructor public class StudentService {
 * 
 * private final StudentRepository studentRepository; private final JwtUtil
 * jwtUtil;
 * 
 * public String login(LoginRequestDto dto) { Student student =
 * studentRepository .findByStudentCodeAndDateOfBirth( dto.getStudentCode(),
 * dto.getDateOfBirth()) .orElseThrow(() -> new
 * RuntimeException("Invalid student credentials"));
 * 
 * return jwtUtil.generateToken(student.getStudentCode()); } // Add Student
 * public Student addStudent(Student student) {
 * 
 * String code;
 * 
 * do { code = "STU" + (1000 + new Random().nextInt(9000)); } while
 * (studentRepository.existsByStudentCode(code));
 * 
 * student.setStudentCode(code);
 * 
 * if (student.getAddresses() != null) { student.getAddresses() .forEach(address
 * -> address.setStudent(student)); }
 * 
 * return studentRepository.save(student); }
 * 
 * // Update Student Profile
 * 
 * @Transactional public Student updateProfile(Long id, Student updatedStudent)
 * {
 * 
 * Student existingStudent = studentRepository.findById(id) .orElseThrow(() ->
 * new RuntimeException("Student not found"));
 * 
 * existingStudent.setName(updatedStudent.getName());
 * existingStudent.setDateOfBirth(updatedStudent.getDateOfBirth());
 * existingStudent.setGender(updatedStudent.getGender());
 * existingStudent.setEmail(updatedStudent.getEmail());
 * existingStudent.setMobileNumber(updatedStudent.getMobileNumber());
 * existingStudent.setFatherName(updatedStudent.getFatherName());
 * existingStudent.setMotherName(updatedStudent.getMotherName());
 * 
 * return studentRepository.save(existingStudent); }
 * 
 * // Get Student By ID public Student getById(Long id) { return
 * studentRepository.findById(id) .orElseThrow(() -> new
 * RuntimeException("Student not found")); }
 * 
 * // Add Addresses
 * 
 * @Transactional public Student addAddresses(Long studentId, List<Address>
 * addresses) {
 * 
 * Student student = studentRepository.findById(studentId) .orElseThrow(() ->
 * new RuntimeException("Student not found"));
 * 
 * for (Address address : addresses) { address.setStudent(student);
 * student.getAddresses().add(address); }
 * 
 * return studentRepository.save(student); }
 * 
 * // Search Student by studentCode public Student getByStudentCode(String
 * studentCode) { return studentRepository.findByStudentCode(studentCode)
 * .orElseThrow(() -> new RuntimeException("Student not found")); }
 * 
 * // Delete Student public void deleteStudent(Long id) { Student student =
 * studentRepository.findById(id) .orElseThrow(() -> new
 * RuntimeException("Student not found"));
 * 
 * student.getAddresses().clear(); // remove from join table
 * studentRepository.save(student);
 * 
 * studentRepository.delete(student); } }
 */

package com.stm.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.stm.dto.LoginRequestDto;
import com.stm.entity.Address;
import com.stm.entity.Student;
import com.stm.exception.InvalidCredentialsException;
import com.stm.exception.StudentNotFoundException;
import com.stm.filter.JwtUtil;
import com.stm.repository.StudentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final JwtUtil jwtUtil;

    // Student Login
    public String login(LoginRequestDto dto) {
        Student student = studentRepository
                .findByStudentCodeAndDateOfBirth(
                        dto.getStudentCode(),
                        dto.getDateOfBirth()
                )
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Invalid student code or date of birth"
                        )
                );

        return jwtUtil.generateToken(student.getStudentCode());
    }

    // Add Student
    public Student addStudent(Student student) {

        String code;

        do {
            code = "STU" + (1000 + new Random().nextInt(9000));
        } while (studentRepository.existsByStudentCode(code));

        student.setStudentCode(code);

        if (student.getAddresses() != null) {
            student.getAddresses().forEach(address ->
                    address.setStudent(student));
        }

        return studentRepository.save(student);
    }

    // Update Student Profile
    @Transactional
    public Student updateProfile(Long id, Student updatedStudent) {

        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student not found with ID: " + id
                        )
                );

        existingStudent.setName(updatedStudent.getName());
        existingStudent.setDateOfBirth(updatedStudent.getDateOfBirth());
        existingStudent.setGender(updatedStudent.getGender());
        existingStudent.setEmail(updatedStudent.getEmail());
        existingStudent.setMobileNumber(updatedStudent.getMobileNumber());
        existingStudent.setFatherName(updatedStudent.getFatherName());
        existingStudent.setMotherName(updatedStudent.getMotherName());

        // Fix for addresses
        if (updatedStudent.getAddresses() != null) {
            existingStudent.getAddresses().clear();

            for (Address address : updatedStudent.getAddresses()) {
                address.setStudent(existingStudent);
                existingStudent.getAddresses().add(address);
            }
        }

        return studentRepository.save(existingStudent);
    }

    // Get Student By ID
    public Student getById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student not found with ID: " + id
                        )
                );
    }

    // Add Addresses
    @Transactional
    public Student addAddresses(Long studentId, List<Address> addresses) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student not found with ID: " + studentId
                        )
                );

        for (Address address : addresses) {
            address.setStudent(student);
            student.getAddresses().add(address);
        }

        return studentRepository.save(student);
    }

    // Search Student by Student Code
    public Student getByStudentCode(String studentCode) {
        return studentRepository.findByStudentCode(studentCode)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student not found with code: " + studentCode
                        )
                );
    }

    // Delete Student
    @Transactional
    public void deleteStudent(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student not found with ID: " + id
                        )
                );

        // Remove child addresses first
        student.getAddresses().clear();
        studentRepository.save(student);

        studentRepository.delete(student);
    }
    
    public Student validateStudent(
            String studentCode,
            String dateOfBirth) {

        return studentRepository
                .findByStudentCodeAndDateOfBirth(
                        studentCode,
                        LocalDate.parse(dateOfBirth)
                )
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Invalid student credentials"
                        )
                );
    }
}