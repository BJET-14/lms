package com.bjet.aki.lms.repository;

import com.bjet.aki.lms.jpa.ModuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ModuleRepository extends JpaRepository<ModuleEntity, Long>, JpaSpecificationExecutor {
}
