package com.bjet.aki.lms.service;

import com.bjet.aki.lms.asset.ListResultBuilder;
import com.bjet.aki.lms.asset.PagedResult;
import com.bjet.aki.lms.asset.PagedResultBuilder;
import com.bjet.aki.lms.asset.ResultBuilder;
import com.bjet.aki.lms.model.Course;
import com.bjet.aki.lms.jpa.CourseEntity;
import com.bjet.aki.lms.mapper.CourseMapper;
import com.bjet.aki.lms.repository.CourseRepository;
import com.bjet.aki.lms.specification.CourseSpecification;
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
    private final CourseMapper courseMapper;

    public void saveCourse(Course course) {
        logger.info("Saving request for course");
        CourseEntity courseEntity = courseMapper.toEntity().map(course);
        courseRepository.save(courseEntity);
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
}
