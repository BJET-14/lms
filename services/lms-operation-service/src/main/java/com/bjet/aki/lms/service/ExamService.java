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
        return ExcelUtils.examResultTemplateToExcel(course, exam, getStudents());
    }

    // todo
    // this generate is to check the download option. Real data will be replaced when student enrollments work
    private List<Student> getStudents() {
        List<Student> students = new ArrayList<>();
        students.add((Student) new Student().setId(1).setFirstName("Obonti").setLastName("Roy"));
        students.add((Student) new Student().setId(2).setFirstName("Nayeem").setLastName("Hossain"));
        students.add((Student) new Student().setId(3).setFirstName("Farhana").setLastName("Jahan"));
        students.add((Student) new Student().setId(4).setFirstName("Tanvir").setLastName("Ahmed"));
        students.add((Student) new Student().setId(5).setFirstName("Mehedi").setLastName("Hasan"));
        students.add((Student) new Student().setId(6).setFirstName("Jarin").setLastName("Akter"));
        students.add((Student) new Student().setId(7).setFirstName("Abir").setLastName("Rahman"));
        students.add((Student) new Student().setId(8).setFirstName("Fatema").setLastName("Tuz Zohra"));
        students.add((Student) new Student().setId(9).setFirstName("Mahfuz").setLastName("Alam"));
        students.add((Student) new Student().setId(10).setFirstName("Nusrat").setLastName("Jahan"));
        students.add((Student) new Student().setId(11).setFirstName("Sakib").setLastName("Amin"));
        return students;
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
                "_e" + examId;
    }

    public boolean isExist(Long examId) {
        return examRepository.findById(examId).isPresent();
    }

    public ExamResultDetails getResultDetails(Long examId) {
        ExamEntity examEntity = examRepository.findById(examId).orElseThrow(() -> new CommonException("05", "Exam not found"));
        ExamResultDetails details = examMapper.toResultDetails().map(examEntity);
        List<ExamResultEntity> resultEntities = examResultRepository.findAllByExamIdOrderByMark(examId);
        List<ExamResult> examResults = ListResultBuilder.build(resultEntities, examMapper.toResultDomain());
        details.setLowestMark(examResults.get(0).getMark());
        details.setHighestMark(examResults.get(examResults.size()-1).getMark());
        details.setResults(examResults);
        return details;
    }
}
