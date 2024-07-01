package com.bjet.aki.lms.repository;

import com.bjet.aki.lms.jpa.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CourseRepository extends JpaRepository<CourseEntity, Long>, JpaSpecificationExecutor {
}
