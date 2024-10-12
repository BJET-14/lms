package com.bjet.aki.lms.mapper;

import com.bjet.aki.lms.asset.ResultMapper;
import com.bjet.aki.lms.jpa.ClassScheduleEntity;
import com.bjet.aki.lms.repository.CourseScheduleRepository;
import com.bjet.aki.lms.model.*;
import com.bjet.aki.lms.jpa.CourseEntity;
import com.bjet.aki.lms.jpa.ModuleEntity;
import com.bjet.aki.lms.model.Module;
import com.bjet.aki.lms.repository.CourseRepository;
import com.bjet.aki.lms.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CourseMapper {

    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final CourseScheduleRepository courseScheduleRepository;
    private final CourseScheduleMapper courseScheduleMapper;

    public ResultMapper<Course, CourseEntity> toEntity() {
        return domain -> {
            CourseEntity courseEntity = courseRepository.findById(domain.getId())
                    .orElseGet(CourseEntity::new);
            if(domain.getTitle() != null){
                courseEntity.setTitle(domain.getTitle());
            }
            if(domain.getDescription() != null){
                courseEntity.setDescription(domain.getDescription());
            }
            if(domain.getStartDate() != null){
                courseEntity.setStartDate(domain.getStartDate());
            }
            if(domain.getEndDate() != null){
                courseEntity.setEndDate(domain.getEndDate());
            }
            if(domain.getClassMeetingLink() != null){
                courseEntity.setClassMeetingLink(domain.getClassMeetingLink());
            }
            if (domain.getModules() != null) {
                courseEntity.setModules(domain.getModules()
                        .stream()
                        .map(module -> moduleDomainToEntity().map(module))
                        .toList());
            }
            return courseEntity;
        };
    }

    public ResultMapper<CourseEntity, Course> toDomain() {
        return entity -> new Course()
                .setId(entity.getId())
                .setTitle(entity.getTitle())
                .setDescription(entity.getDescription())
                .setStartDate(entity.getStartDate())
                .setEndDate(entity.getEndDate())
                .setModules(entity.getModules()
                        .stream()
                        .map(moduleEntity -> moduleEntityToDomain().map(moduleEntity))
                        .toList())
                .setTeacherId(entity.getTeacherId())
                .setClassMeetingLink(entity.getClassMeetingLink());
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

    public ClassScheduleSentToEmailRequest toEmailNotificationToTeacherWithClassSchedule(String receiverEmailAddress, Teacher teacher, CourseEntity course, List<ClassScheduleEntity> courseSchedules) {
        ClassScheduleSentToEmailRequest request = new ClassScheduleSentToEmailRequest();
        request.setReceiverEmailAddress(receiverEmailAddress);
        request.setReceiverName(teacher.getFirstName() + " " + teacher.getLastName());
        request.setTeacherName(teacher.getFirstName() + " " + teacher.getLastName());
        request.setCourseTitle(course.getTitle());
        List<DetailClassSchedule> detailClassSchedules = new ArrayList<>();
        List<ModuleEntity> modules = course.getModules();
        for(int i=0; i < modules.size(); i++){
            ModuleEntity module = modules.get(i);
            ClassScheduleEntity schedule = courseSchedules.get(i);
            DetailClassSchedule detailClassSchedule = new DetailClassSchedule();
            detailClassSchedule.setId(schedule.getId());
            detailClassSchedule.setTitle(module.getTitle());
            detailClassSchedule.setDate(schedule.getClassDateTime().toLocalDate());
            detailClassSchedule.setTime(schedule.getClassDateTime().toLocalTime());
            detailClassSchedules.add(detailClassSchedule);
        }
        request.setClassSchedules(detailClassSchedules);
        request.setSendingToStudent(!receiverEmailAddress.equals(teacher.getEmail()));
        return request;
    }
}
