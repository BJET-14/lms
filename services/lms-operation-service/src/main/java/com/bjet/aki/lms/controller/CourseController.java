package com.bjet.aki.lms.controller;

import com.bjet.aki.lms.asset.PagedResult;
import com.bjet.aki.lms.model.Course;
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
    public ResponseEntity<Void> saveCourse(@RequestBody Course course) {
        courseService.saveCourse(course);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> findCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.findCourseById(id));
    }
}
