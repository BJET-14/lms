package com.bjet.aki.lms.repository;

import com.bjet.aki.lms.jpa.ExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamRepository extends JpaRepository<ExamEntity, Long> {
    List<ExamEntity> findAllByCourseId(Long courseId);
}
