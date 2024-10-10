package com.bjet.aki.lms.controller;

import com.bjet.aki.lms.model.Exam;
import com.bjet.aki.lms.model.ExamResult;
import com.bjet.aki.lms.model.ExamResultDetails;
import com.bjet.aki.lms.service.CourseService;
import com.bjet.aki.lms.service.ExamService;
import com.bjet.aki.lms.util.ExcelUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/operations/courses/{courseId}/exams")
@Tag(name = "Exam Controller",
        description = "Exam related api requests.")
@AllArgsConstructor
public class ExamController {

    private final CourseService courseService;
    private final ExamService examService;

    @PostMapping
    public ResponseEntity<Long> save(@PathVariable("courseId") Long courseId, @RequestBody Exam request){
        if(!courseService.isExist(courseId)){
            return ResponseEntity.notFound().build();
        }
        Long id = examService.save(courseId, request);
        return ResponseEntity.ok(id);
    }

    @GetMapping
    public ResponseEntity<List<Exam>> getExamsByCourse(@PathVariable("courseId") Long courseId){
        if(!courseService.isExist(courseId)){
            return ResponseEntity.notFound().build();
        }
        List<Exam> exams = examService.getExams(courseId);
        return ResponseEntity.ok(exams);
    }

    @PostMapping(path = "/{examId}/upload-results")
    public ResponseEntity<Void> uploadResults(@PathVariable("courseId") Long courseId,
                                              @PathVariable("examId") Long examId,
                                              @RequestParam("file") MultipartFile file){
        if(!courseService.isExist(courseId)){
            return ResponseEntity.notFound().build();
        }
        if (ExcelUtils.hasExcelFormat(file)) {
            try {
                examService.saveResult(file, examId);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
            }
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{examId}/template")
    public ResponseEntity<Resource> getFile(@PathVariable("courseId") Long courseId, @PathVariable Long examId) {
        if(!courseService.isExist(courseId)){
            return ResponseEntity.notFound().build();
        }
        if(!examService.isExist(examId)){
            return ResponseEntity.notFound().build();
        }
        String filename = examService.generateFileName(courseId, examId);
        InputStreamResource file = new InputStreamResource(examService.load(courseId, examId));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

    @GetMapping(path = "{examId}/result")
    public ResponseEntity<ExamResultDetails> getResults(@PathVariable("courseId") Long courseId,
                                                        @PathVariable Long examId) {
        if(!courseService.isExist(courseId)){
            return ResponseEntity.notFound().build();
        }
        if(!examService.isExist(examId)){
            return ResponseEntity.notFound().build();
        }
        ExamResultDetails details = examService.getResultDetails(examId);
        return ResponseEntity.ok(details);
    }
}
