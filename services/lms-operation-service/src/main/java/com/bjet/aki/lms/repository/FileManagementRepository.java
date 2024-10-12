package com.bjet.aki.lms.repository;

import com.bjet.aki.lms.jpa.FileManagementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FileManagementRepository extends JpaRepository<FileManagementEntity, Long>, JpaSpecificationExecutor {
}
