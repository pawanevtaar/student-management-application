package com.stm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stm.entity.Enrollment;

public interface EnrollmentRepository
        extends JpaRepository<Enrollment, Long> {


    List<Enrollment> findByCourseId(Long courseId);

    Optional<Enrollment> findByStudentIdAndCourseId(
            Long studentId,
            Long courseId
    );

	List<Enrollment> findByStudentId(Long studentId);
}