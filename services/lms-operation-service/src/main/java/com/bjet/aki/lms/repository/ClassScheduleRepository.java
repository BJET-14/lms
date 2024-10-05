package com.bjet.aki.lms.repository;

import com.bjet.aki.lms.jpa.ClassScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassScheduleRepository extends JpaRepository<ClassScheduleEntity, Long> {
    List<ClassScheduleEntity> findAllByCourse_Id(Long courseId);
}
