package com.stm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.stm.entity.Address;
import com.stm.entity.Student;
import com.stm.exception.StudentNotFoundException;
import com.stm.repository.StudentRepository;
import com.stm.service.StudentService;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setName("Pawan");
        student.setEmail("pawan@gmail.com");
        student.setMobileNumber("9876543210");
        student.setDateOfBirth(LocalDate.of(2000, 1, 1));
        student.setAddresses(new ArrayList<>());
    }

    @Test
    void testCreateStudent() {
        when(studentRepository.save(student)).thenReturn(student);

        Student savedStudent = studentService.addStudent(student);

        assertNotNull(savedStudent);
        assertEquals("Pawan", savedStudent.getName());
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void testGetStudentById() {
        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        Student result = studentService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Pawan", result.getName());
    }

    @Test
    void testGetStudentById_NotFound() {
        when(studentRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class,
                () -> studentService.getById(1L));
    }

    @Test
    void testUpdateProfile() {
        Student updatedStudent = new Student();
        updatedStudent.setName("Updated Pawan");
        updatedStudent.setEmail("updated@gmail.com");
        updatedStudent.setMobileNumber("9999999999");
        updatedStudent.setDateOfBirth(LocalDate.of(2001, 2, 2));
        updatedStudent.setAddresses(new ArrayList<>());

        Address address = new Address();
        address.setCity("Patna");
        updatedStudent.getAddresses().add(address);

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        when(studentRepository.save(any(Student.class)))
                .thenReturn(student);

        Student result = studentService.updateProfile(1L, updatedStudent);

        assertNotNull(result);
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void testDeleteStudent() {
        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        doNothing().when(studentRepository).delete(student);

        studentService.deleteStudent(1L);

        verify(studentRepository, times(1)).delete(student);
    }

    @Test
    void testDeleteStudent_NotFound() {
        when(studentRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class,
                () -> studentService.deleteStudent(1L));
    }
}