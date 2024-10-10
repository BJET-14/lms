package com.bjet.aki.lms.service;

import com.bjet.aki.lms.asset.ListResultBuilder;
import com.bjet.aki.lms.exception.CommonException;
import com.bjet.aki.lms.jpa.ExamEntity;
import com.bjet.aki.lms.jpa.ExamResultEntity;
import com.bjet.aki.lms.mapper.ExamMapper;
import com.bjet.aki.lms.model.*;
import com.bjet.aki.lms.repository.ExamRepository;
import com.bjet.aki.lms.repository.ExamResultRepository;
import com.bjet.aki.lms.util.ExcelUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.text.StrBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ExamService {

    private final ExamRepository examRepository;
    private final ExamResultRepository examResultRepository;
    private final ExamMapper examMapper;
    private final CourseService courseService;

    public Long save(Long courseId, Exam request) {
        log.info("Saving exams for course. Id: {}", courseId);
        ExamEntity exam = examRepository.save(examMapper.toEntity(courseId).map(request));
        return exam.getId();
    }

    public List<Exam> getExams(Long courseId) {
        log.info("Getting exams for course. Id: {}", courseId);
        List<ExamEntity> exams = examRepository.findAllByCourseId(courseId);
        return ListResultBuilder.build(exams, examMapper.toDomain());
    }

    public ByteArrayInputStream load(Long courseId, Long examId) {
        log.info("Loading template excel for result upload. Course Id: {}, Exam Id: {}", courseId, examId);
        Course course = courseService.findCourseById(courseId);
        ExamEntity exam = examRepository.findById(examId).orElseThrow(() -> new CommonException("05", "Exam not found"));
        List<Student> students = courseService.findStudentsByCourse(courseId);
        return ExcelUtils.examResultTemplateToExcel(course, exam, students);
    }

    public void saveResult(MultipartFile file, Long examId) {
        try {
            List<ExamResultEntity> results = ExcelUtils.excelToExamResult(file.getInputStream(), examId);
            examResultRepository.saveAll(results);
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

    public String generateFileName(Long courseId, Long examId) {
        return "result-template" +
                "_c" + courseId +
                "_e" + examId + ".xlsx";
    }

    public boolean isExist(Long examId) {
        return examRepository.findById(examId).isPresent();
    }

    public ExamResultDetails getResultDetails(Long examId) {
        ExamEntity examEntity = examRepository.findById(examId).orElseThrow(() -> new CommonException("05", "Exam not found"));
        ExamResultDetails details = examMapper.toResultDetails().map(examEntity);
        List<ExamResultEntity> resultEntities = examResultRepository.findAllByExamIdOrderByMark(examId);
        List<ExamResult> examResults = ListResultBuilder.build(resultEntities, examMapper.toResultDomain());
        details.setLowestMark(examResults.size() != 0 ? examResults.get(0).getMark(): 0);
        details.setHighestMark(examResults.size() != 0 ? examResults.get(examResults.size()-1).getMark() : 0);
        details.setResults(examResults);
        return details;
    }
}
