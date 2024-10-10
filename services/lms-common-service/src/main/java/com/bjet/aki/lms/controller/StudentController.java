package com.bjet.aki.lms.controller;

import com.bjet.aki.lms.model.Student;
import com.bjet.aki.lms.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commons/students")
@AllArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/{studentId}")
    public ResponseEntity<?> findTeacherById(@PathVariable Long studentId) {
        if(!studentService.isExist(studentId)){
            return ResponseEntity.notFound().build();
        }
        Student student = studentService.getStudent(studentId);
        return ResponseEntity.ok(student);
    }
}
