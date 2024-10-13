package com.bjet.aki.lms.repository;

import com.bjet.aki.lms.jpa.StudentEnrollmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentEnrollmentRepository extends JpaRepository<StudentEnrollmentEntity, Long> {
    List<StudentEnrollmentEntity> findAllByCourseId(Long courseId);
    boolean existsByCourseIdAndStudentId(Long courseId, Long studentId);
}
