package com.bjet.aki.lms.repository;

import com.bjet.aki.lms.jpa.TeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<TeacherEntity, Long> {
}
