package com.bjet.aki.lms.controller;

import com.bjet.aki.lms.asset.PagedResult;
import com.bjet.aki.lms.model.Teacher;
import com.bjet.aki.lms.service.TeacherService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/commons/teachers")
@Tag(name = "Teacher Controller",
        description = "Teacher related api requests.")
@AllArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping
    public ResponseEntity<?> findAllTeachers(
            @RequestParam(name = "asPage", required = false, defaultValue = "false") Boolean asPage,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(name = "firstName", required = false) String firstName,
            @RequestParam(name = "lastName", required = false) String lastName,
            @RequestParam(name = "email", required = false) String email
    ){
        if(asPage){
            Pageable pageable = PageRequest.of(page, size);
            PagedResult<Teacher> teacherAsPage = teacherService.findAllTeachers(pageable, firstName, lastName, email);
            return ResponseEntity.ok(teacherAsPage);
        }
        List<Teacher> teacherAsList = teacherService.findAllTeachers(firstName, lastName, email);
        return ResponseEntity.ok(teacherAsList);
    }

    @PutMapping(path = "/{teacherId}/update")
    public ResponseEntity<Void> updateTeacher(@PathVariable("teacherId") Long teacherId,
                                              @RequestBody Teacher teacher){
        if(teacherService.isExist(teacherId)){
            teacherService.update(teacher);
            return ResponseEntity.accepted().build();
        }
        return ResponseEntity.notFound().build();
    }
}
