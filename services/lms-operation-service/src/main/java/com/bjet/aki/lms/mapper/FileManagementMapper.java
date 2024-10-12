package com.bjet.aki.lms.mapper;

import com.bjet.aki.lms.asset.ResultMapper;
import com.bjet.aki.lms.jpa.FileManagementEntity;
import com.bjet.aki.lms.model.FileManagement;
import com.bjet.aki.lms.repository.FileManagementRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FileManagementMapper {

    private final FileManagementRepository fileManagementRepository;

    public ResultMapper<FileManagementEntity, FileManagement> toDomain(){
        return entity -> new FileManagement()
                .setId(entity.getId())
                .setName(entity.getName())
                .setUrl(entity.getUrl())
                .setType(entity.getType())
                .setCourseId(entity.getCourseId())
                .setModuleId(entity.getModuleId());
    }

    public ResultMapper<FileManagement, FileManagementEntity> toEntity(){
        return domain -> fileManagementRepository.findById(domain.getId())
                .orElseGet(FileManagementEntity::new)
                .setCourseId(domain.getCourseId())
                .setModuleId(domain.getModuleId())
                .setName(domain.getName())
                .setUrl(domain.getUrl())
                .setType(domain.getType());
    }
}
