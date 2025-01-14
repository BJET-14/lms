package com.bjet.aki.lms.service;

import com.bjet.aki.lms.asset.ListResultBuilder;
import com.bjet.aki.lms.exception.CommonException;
import com.bjet.aki.lms.jpa.ExamEntity;
import com.bjet.aki.lms.jpa.ExamResultEntity;
import com.bjet.aki.lms.mapper.ExamMapper;
import com.bjet.aki.lms.model.*;
import com.bjet.aki.lms.repository.ExamRepository;
import com.bjet.aki.lms.repository.ExamResultRepository;
import com.bjet.aki.lms.util.CsvUtils;
import com.bjet.aki.lms.util.ExcelUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ExamService {

    private final ExamRepository examRepository;
    private final ExamResultRepository examResultRepository;
    private final ExamMapper examMapper;
    private final CourseService courseService;
    private final FileManagementService fileManagementService;
    private final EmailNotificationService emailNotificationService;
    private final UserService userService;

    public Long save(Long courseId, Exam request) {
        log.info("Saving exams for course. Id: {}", courseId);
        ExamEntity exam = examRepository.save(examMapper.toEntity(courseId).map(request));
        List<StudentEnrollment> enrolledStudent = courseService.getAllEnrolledStudents(courseId);
        if (!enrolledStudent.isEmpty()) {
            for(StudentEnrollment studentEnrollment : enrolledStudent) {
                Student student = userService.findStudent(studentEnrollment.getStudentId());
                ExamLinkSentToStudentRequest emailRequest = examMapper.toEmailRequest(exam, student);
                emailNotificationService.sendEmailWithExamLinkToStudent(emailRequest);
            }
        }
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
//        return ExcelUtils.examResultTemplateToExcel(course, exam, students);
        return CsvUtils.examResultTemplateToCsv(course, exam, students);
    }

    public void saveResult(MultipartFile file, Long examId) {
        try {
            GoogleDriveUploadResponse response = fileManagementService.uploadFilesToGoogleDrive(file, FileType.EXCEL);
            log.info("Upload response. File name: {}, response: {}", file.getOriginalFilename(), (response.getStatus() == 200 ? "Success" : "Fail"));
            List<ExamResultEntity> results = CsvUtils.csvToExamResult(file.getInputStream(), examId);
            examResultRepository.saveAll(results);
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

    public String generateFileName(Long courseId, Long examId) {
        return "result-template" +
                "_c" + courseId +
                "_e" + examId + ".csv";
    }

    public boolean isExist(Long examId) {
        return examRepository.findById(examId).isPresent();
    }

    public ExamResultDetails getResultDetails(Long examId) {
        ExamEntity examEntity = examRepository.findById(examId).orElseThrow(() -> new CommonException("05", "Exam not found"));
        ExamResultDetails details = examMapper.toResultDetails().map(examEntity);
        List<ExamResultEntity> resultEntities = examResultRepository.findAllByExamIdOrderByStudentId(examId);

        Double minScore = !resultEntities.isEmpty() ? resultEntities.stream().map(ExamResultEntity::getMark)
                .min(Comparator.comparing(Double::valueOf))
                .get(): 0;
        Double maxScore = !resultEntities.isEmpty() ? resultEntities.stream().map(ExamResultEntity::getMark)
                .max(Comparator.comparing(Double::valueOf))
                .get(): 0;
        details.setLowestMark(minScore);
        details.setHighestMark(maxScore);
        List<ExamResult> examResults = ListResultBuilder.build(resultEntities, examMapper.toResultDomain());
        details.setResults(examResults);
        return details;
    }
}
