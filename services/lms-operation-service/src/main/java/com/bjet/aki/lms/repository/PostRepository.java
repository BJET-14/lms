package com.bjet.aki.lms.repository;

import com.bjet.aki.lms.jpa.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    List<PostEntity> findAllByCourseIdOrderById(Long courseId);
}
