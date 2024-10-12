package com.bjet.aki.lms.controller;

import com.bjet.aki.lms.asset.PagedResult;
import com.bjet.aki.lms.model.FileManagement;
import com.bjet.aki.lms.service.CourseService;
import com.bjet.aki.lms.service.FileManagementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/operations/courses/{courseId}/files")
@Tag(name = "File Management Controller",
        description = "File management related api requests.")
@AllArgsConstructor
public class FileManagementController {

    private final FileManagementService fileManagementService;
    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<?> getFileManagement(@PathVariable("courseId") Long courseId,
                                                                  @RequestParam(name = "asPage", required = false, defaultValue = "false") Boolean asPage,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "20") int size,
                                                                  @RequestParam(value = "moduleId", required = false) Long moduleId) {
        if(!courseService.isExist(courseId)){
            return ResponseEntity.notFound().build();
        }
        if (asPage) {
            Pageable pageable = PageRequest.of(page, size);
            PagedResult<FileManagement> filesAsPage = fileManagementService.findAllFiles(pageable, courseId, moduleId);
            return ResponseEntity.ok(filesAsPage);
        }
        List<FileManagement> filesAsList = fileManagementService.findAllFiles(courseId, moduleId);
        return ResponseEntity.ok(filesAsList);
    }

    @PostMapping
    public ResponseEntity<?> uploadFile(@PathVariable("courseId") Long courseId,
                                        @RequestParam(value = "moduleId", required = false) Long moduleId,
                                        @RequestParam("file") MultipartFile file){
        if(!courseService.isExist(courseId)){
            return ResponseEntity.notFound().build();
        }
        fileManagementService.uploadFile(courseId, moduleId, file);
        return ResponseEntity.ok().build();
    }

}
