package com.bjet.aki.lms.repository;

import com.bjet.aki.lms.jpa.ExamResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamResultRepository extends JpaRepository<ExamResultEntity, Long> {
    List<ExamResultEntity> findAllByExamIdOrderByMark(Long examId);
}
