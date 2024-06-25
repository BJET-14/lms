package com.bjet.aki.lms.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commons/courses")
@Tag(name = "Course Controller",
        description = "Course related api requests.")
@AllArgsConstructor
public class CourseController {

    @GetMapping
    public ResponseEntity<String> getStarted(){
        return ResponseEntity.ok("Hello from Commons service");
    }
}
