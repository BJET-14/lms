package com.bjet.aki.lms.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseScheduleRepository extends JpaRepository<CourseScheduleEntity, Long> {
    List<CourseScheduleEntity> findAllByCourse_Id(Long courseId);
}
