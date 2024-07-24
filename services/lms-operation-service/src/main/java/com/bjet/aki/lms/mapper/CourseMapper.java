package com.bjet.aki.lms.mapper;

import com.bjet.aki.lms.asset.ResultMapper;
import com.bjet.aki.lms.model.Course;
import com.bjet.aki.lms.jpa.CourseEntity;
import com.bjet.aki.lms.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseMapper {

    private final CourseRepository courseRepository;

    public ResultMapper<Course, CourseEntity> toEntity() {
        return domain -> courseRepository.findById(domain.getId())
                .orElseGet(CourseEntity::new)
                .setTitle(domain.getTitle())
                .setDescription(domain.getDescription())
                .setStartDate(domain.getStartDate());
    }

    public ResultMapper<CourseEntity, Course> toDomain() {
        return entity -> new Course()
                .setId(entity.getId())
                .setTitle(entity.getTitle())
                .setDescription(entity.getDescription())
                .setStartDate(entity.getStartDate())
                .setEndDate(entity.getEndDate())
                .setComplete(entity.getIsComplete());
    }
}
