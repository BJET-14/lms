package com.bjet.aki.lms.repository;

import com.bjet.aki.lms.jpa.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
}
