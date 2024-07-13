package com.bjet.aki.lms.mapper;

import com.bjet.aki.lms.asset.ResultMapper;
import com.bjet.aki.lms.domain.Module;
import com.bjet.aki.lms.jpa.ModuleEntity;
import com.bjet.aki.lms.repository.CourseRepository;
import com.bjet.aki.lms.repository.ModuleRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModuleMapper {

    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;

    public ResultMapper<Module, ModuleEntity> toEntity() {
        return domain -> moduleRepository.findById(domain.getId())
                .orElseGet(ModuleEntity::new)
                .setTitle(domain.getTitle())
                .setDescription(domain.getDescription())
                .setCourse(courseRepository.findById(domain.getCourseId()).orElseThrow(() -> new NotFoundException("Could not find course with id : " + domain.getCourseId())));
    }

    public ResultMapper<ModuleEntity, Module> toDomain() {
        return entity -> new Module()
                .setId(entity.getId())
                .setTitle(entity.getTitle())
                .setDescription(entity.getDescription())
                .setCourseId(entity.getCourse().getId());
    }
}
