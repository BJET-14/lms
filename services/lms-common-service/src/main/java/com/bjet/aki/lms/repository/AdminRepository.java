package com.bjet.aki.lms.repository;

import com.bjet.aki.lms.jpa.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<AdminEntity, Long> {
}
