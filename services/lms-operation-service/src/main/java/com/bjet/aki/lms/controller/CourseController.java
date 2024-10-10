package com.bjet.aki.lms.controller;

import com.bjet.aki.lms.asset.PagedResult;
import com.bjet.aki.lms.model.*;
import com.bjet.aki.lms.service.CourseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/operations/courses")
@Tag(name = "Course Controller",
        description = "Course related api requests.")
@AllArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<?> getCourses(
            @RequestParam(name = "asPage", required = false, defaultValue = "false") Boolean asPage,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "startDateFrom", required = false) LocalDate startDateFrom,
            @RequestParam(name = "startDateTo", required = false) LocalDate startDateTo,
            @RequestParam(name = "isComplete", required = false) Boolean isComplete) {
        if (asPage) {
            Pageable pageable = PageRequest.of(page, size);
            PagedResult<Course> productsAsPage = courseService.findAllCourses(pageable, title, startDateFrom, startDateTo, isComplete);
            return ResponseEntity.ok(productsAsPage);
        }
        List<Course> productsAsList = courseService.findAllCourses(title, startDateFrom, startDateTo, isComplete);
        return ResponseEntity.ok(productsAsList);
    }

    @PostMapping
    public ResponseEntity<Long> saveCourse(@RequestBody Course course) {
        Long id = courseService.saveCourse(course);
        return ResponseEntity.ok(id);
    }

    @PutMapping(path = "/{courseId}")
    public ResponseEntity<Void> updateCourse(@PathVariable(name = "courseId") Long id, @RequestBody Course course) {
        boolean isExist = courseService.isExist(id);
        if(isExist){
            courseService.updateCourse(course);
            return ResponseEntity.accepted().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> findCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.findCourseById(id));
    }

    @DeleteMapping(path = "/{courseId}/modules/{moduleId}")
    public ResponseEntity<Void> deleteModule(@PathVariable(name = "courseId") Long courseId,
                                             @PathVariable(name = "moduleId") Long moduleId){
        boolean courseExist = courseService.isExist(courseId);
        if(courseExist){
            courseService.deleteModule(moduleId);
            return ResponseEntity.accepted().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(path = "/{courseId}/assign")
    public ResponseEntity<Void> assignTeacherToCourse(@PathVariable("courseId") Long courseId, @RequestBody TeacherAssigningToCourseRequest request){
        if(!courseService.isExist(courseId)){
            return ResponseEntity.notFound().build();
        }
        courseService.assignTeacherToCourse(courseId, request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping(path = "{courseId}/schedule")
    public ResponseEntity<Void> scheduleCourse(@PathVariable Long courseId, @RequestBody CourseScheduleRequest request){
        if(!courseService.isExist(courseId)){
            return ResponseEntity.notFound().build();
        }
        courseService.schedule(courseId, request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping(path = "{courseId}/class-schedule")
    public ResponseEntity<List<ClassSchedule>> getCourseSchedule(@PathVariable Long courseId){
        List<ClassSchedule> schedule = courseService.getClassSchedule(courseId);
        return ResponseEntity.ok().body(schedule);
    }

    @PostMapping(path = "{courseId}/enrollement")
    public ResponseEntity<Long> enrollmentToCourse(@PathVariable Long courseId, @RequestBody StudentEnrollmentRequest request){
        if(!courseService.isExist(courseId)){
            return ResponseEntity.notFound().build();
        }
        Long id = courseService.enroll(courseId, request);
        return ResponseEntity.ok(id);
    }

    @GetMapping(path = "{courseId}/enrollment")
    public ResponseEntity<List<StudentEnrollment>> getEnrolledStudents(@PathVariable Long courseId){
        if(!courseService.isExist(courseId)){
            return ResponseEntity.notFound().build();
        }
        List<StudentEnrollment> studentEnrollments = courseService.getAllEnrolledStudents(courseId);
        return ResponseEntity.ok().body(studentEnrollments);
    }
}
