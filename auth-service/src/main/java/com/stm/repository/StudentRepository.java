package com.stm.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stm.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByStudentCode(String studentCode);

    Optional<Student> findByStudentCodeAndDateOfBirth(
            String studentCode,
            LocalDate dateOfBirth);

	boolean existsByStudentCode(String studentCode);
}