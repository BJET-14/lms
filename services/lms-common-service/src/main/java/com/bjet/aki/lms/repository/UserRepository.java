package com.bjet.aki.lms.repository;

import com.bjet.aki.lms.jpa.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor {
    Optional<UserEntity> findByEmail(String email);
}
