package com.bjet.aki.lms.mapper;

import com.bjet.aki.lms.asset.ResultMapper;
import com.bjet.aki.lms.jpa.ExamEntity;
import com.bjet.aki.lms.jpa.ExamResultEntity;
import com.bjet.aki.lms.model.Exam;
import com.bjet.aki.lms.model.ExamResult;
import com.bjet.aki.lms.model.ExamResultDetails;
import com.bjet.aki.lms.repository.ExamRepository;
import com.bjet.aki.lms.repository.ExamResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExamMapper {

    private final ExamRepository repository;
    private final ExamResultRepository resultRepository;

    public ResultMapper<Exam, ExamEntity> toEntity(Long courseId){
        return domain -> repository.findById(domain.getId())
                .orElseGet(ExamEntity::new)
                .setName(domain.getName())
                .setType(domain.getType())
                .setDescription(domain.getDescription())
                .setGoogleFormLink(domain.getGoogleFormLink())
                .setFullMarks(domain.getFullMark())
                .setCourseId(courseId);
    }

    public ResultMapper<ExamEntity, Exam> toDomain(){
        return entity -> new Exam()
                .setId(entity.getId())
                .setName(entity.getName())
                .setType(entity.getType())
                .setDescription(entity.getDescription())
                .setGoogleFormLink(entity.getGoogleFormLink())
                .setFullMark(entity.getFullMarks());
    }

    public ResultMapper<ExamResult, ExamResultEntity> toResultEntity(){
        return domain -> resultRepository.findById(domain.getId())
                .orElseGet(ExamResultEntity::new)
                .setId(domain.getId())
                .setExamId(domain.getExamId())
                .setStudentId(domain.getStudentId())
                .setMark(domain.getMark())
                .setComment(domain.getComment());
    }

    public ResultMapper<ExamResultEntity, ExamResult> toResultDomain(){
        return entity -> new ExamResult()
                .setId(entity.getId())
                .setExamId(entity.getExamId())
                .setStudentId(entity.getStudentId())
                .setMark(entity.getMark())
                .setComment(entity.getComment());
    }

    public ResultMapper<ExamEntity, ExamResultDetails> toResultDetails() {
        return entity -> new ExamResultDetails()
                .setExamId(entity.getId())
                .setExamName(entity.getName())
                .setExamType(entity.getType())
                .setFullMark(entity.getFullMarks());

    }
}
