package com.bjet.aki.lms.mapper;

import com.bjet.aki.lms.asset.ResultMapper;
import com.bjet.aki.lms.jpa.CourseScheduleEntity;
import com.bjet.aki.lms.repository.CourseScheduleRepository;
import com.bjet.aki.lms.model.CourseSchedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CourseScheduleMapper {

    private final CourseScheduleRepository repository;

    public ResultMapper<CourseSchedule, CourseScheduleEntity> toEntity(){
        return domain -> repository.findById(domain.getId())
                .orElseGet(CourseScheduleEntity::new)
                .setDays(domain.getDays())
                .setTimeSlot(domain.getTimeSlot());
    }

    public ResultMapper<CourseScheduleEntity, CourseSchedule> toDomain(){
        return entity -> new CourseSchedule()
                .setId(entity.getId())
                .setDays(entity.getDays())
                .setTimeSlot(entity.getTimeSlot());
    }
}
