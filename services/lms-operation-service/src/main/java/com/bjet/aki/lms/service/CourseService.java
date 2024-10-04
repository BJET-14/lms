package com.bjet.aki.lms.service;

import com.bjet.aki.lms.asset.ListResultBuilder;
import com.bjet.aki.lms.asset.PagedResult;
import com.bjet.aki.lms.asset.PagedResultBuilder;
import com.bjet.aki.lms.asset.ResultBuilder;
import com.bjet.aki.lms.exception.CommonException;
import com.bjet.aki.lms.jpa.CourseScheduleEntity;
import com.bjet.aki.lms.jpa.CourseScheduleRepository;
import com.bjet.aki.lms.mapper.CourseScheduleMapper;
import com.bjet.aki.lms.model.*;
import com.bjet.aki.lms.jpa.CourseEntity;
import com.bjet.aki.lms.jpa.ModuleEntity;
import com.bjet.aki.lms.mapper.CourseMapper;
import com.bjet.aki.lms.repository.CourseRepository;
import com.bjet.aki.lms.repository.ModuleRepository;
import com.bjet.aki.lms.specification.CourseSpecification;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    Logger logger = LoggerFactory.getLogger(CourseService.class);

    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final CourseMapper courseMapper;
    private final UserService userService;
    private final CourseScheduleMapper courseScheduleMapper;
    private final CourseScheduleRepository courseScheduleRepository;

    @Transactional
    public void saveCourse(Course course) {
        logger.info("Saving request for course");
        CourseEntity courseEntity = courseMapper.toEntity().map(course);
        courseRepository.save(courseEntity);

        // Save the modules associated with the course
        List<ModuleEntity> modules = courseEntity.getModules();
        if (modules != null) {
            for (ModuleEntity module : modules) {
                module.setCourse(courseEntity);
                moduleRepository.save(module);
            }
        }
    }

    public List<Course> findAllCourses(String title, LocalDate startDateFrom, LocalDate startDateTo, Boolean isComplete) {
        List<CourseEntity> courses = courseRepository.findAll(CourseSpecification.findCourses(title, startDateFrom, startDateTo, isComplete));
        return ListResultBuilder.build(courses, courseMapper.toDomain());
    }

    public PagedResult<Course> findAllCourses(Pageable pageable, String title, LocalDate startDateFrom, LocalDate startDateTo, Boolean isComplete) {
        Page<CourseEntity> courses = courseRepository.findAll(CourseSpecification.findCourses(title, startDateFrom, startDateTo, isComplete), pageable);
        return PagedResultBuilder.build(courses, courseMapper.toDomain());
    }

    public Course findCourseById(Long id) {
        CourseEntity courseEntity = courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Could not find course with id: " + id));
        return ResultBuilder.build(courseEntity, courseMapper.toDomain());
    }

    public void updateCourse(Course course) {
        CourseEntity courseEntity = courseMapper.toEntity().map(course);
        courseRepository.save(courseEntity);
    }

    public boolean isExist(long id) {
        return courseRepository.existsById(id);
    }

    public void deleteModule(Long moduleId) {
        moduleRepository.deleteById(moduleId);
    }

    @Transactional
    public void assignTeacherToCourse(Long courseId, TeacherAssigningToCourseRequest request) {
        logger.info("Assigning teacher to course. CourseId {}", courseId);
        CourseEntity entity = courseRepository.findById(courseId).orElseThrow(() -> new CommonException("03", "Could not find course"));
        Teacher teacher = userService.findTeacher(request.getTeacherId());
        if(teacher == null) {
            throw new CommonException("02", "Could not find teacher");
        }
        entity.setTeacherId(request.getTeacherId());
        courseRepository.save(entity);
    }

    @Transactional
    public void schedule(Long courseId, CourseScheduleRequest request) {
        logger.info("Scheduling the course. CourseId {}", courseId);
        CourseEntity course = courseRepository.findById(courseId).orElseThrow(() -> new CommonException("03", "Could not find course"));
        List<CourseSchedule> schedules = request.getSchedules();
        List<CourseScheduleEntity> scheduleEntities = schedules.stream()
                .map(schedule -> {
                    CourseScheduleEntity entity = courseScheduleMapper.toEntity().map(schedule);
                    entity.setCourse(course);
                    return entity;
                })
                .toList();
        courseScheduleRepository.saveAll(scheduleEntities);
    }

    public List<CourseSchedule> getSchedule(Long courseId) {
        List<CourseScheduleEntity> schedules = courseScheduleRepository.findAllByCourse_Id(courseId);
        return schedules.stream().map(entity -> courseScheduleMapper.toDomain().map(entity)).toList();
    }
}
