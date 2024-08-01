package com.bjet.aki.lms.mapper;

import com.bjet.aki.lms.asset.ResultMapper;
import com.bjet.aki.lms.domain.Course;
import com.bjet.aki.lms.domain.Module;
import com.bjet.aki.lms.model.Course;
import com.bjet.aki.lms.jpa.CourseEntity;
import com.bjet.aki.lms.jpa.ModuleEntity;
import com.bjet.aki.lms.repository.CourseRepository;
import com.bjet.aki.lms.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseMapper {

    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;

    public ResultMapper<Course, CourseEntity> toEntity() {
        return domain -> courseRepository.findById(domain.getId())
                .orElseGet(CourseEntity::new)
                .setTitle(domain.getTitle())
                .setDescription(domain.getDescription())
                .setStartDate(domain.getStartDate())
                .setEndDate(domain.getEndDate())
                .setIsComplete(domain.isComplete())
                .setModules(domain.getModules()
                        .stream()
                        .map(module -> moduleDomainToEntity().map(module))
                        .toList());
    }

    public ResultMapper<CourseEntity, Course> toDomain() {
        return entity -> new Course()
                .setId(entity.getId())
                .setTitle(entity.getTitle())
                .setDescription(entity.getDescription())
                .setStartDate(entity.getStartDate())
                .setEndDate(entity.getEndDate())
                .setComplete(entity.getIsComplete())
                .setModules(entity.getModules()
                        .stream()
                        .map(moduleEntity -> moduleEntityToDomain().map(moduleEntity))
                        .toList());
    }

    public ResultMapper<Module, ModuleEntity> moduleDomainToEntity() {
        return domain -> moduleRepository.findById(domain.getId())
                .orElseGet(ModuleEntity::new)
                .setTitle(domain.getTitle())
                .setDescription(domain.getDescription());
    }

    public ResultMapper<ModuleEntity, Module> moduleEntityToDomain() {
        return domain -> new Module()
                .setId(domain.getId())
                .setTitle(domain.getTitle())
                .setDescription(domain.getDescription());
    }
}
