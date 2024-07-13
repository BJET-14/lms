package com.bjet.aki.lms.mapper;

import com.bjet.aki.lms.asset.ResultMapper;
import com.bjet.aki.lms.domain.AcademicQualification;
import com.bjet.aki.lms.domain.Teacher;
import com.bjet.aki.lms.domain.Training;
import com.bjet.aki.lms.jpa.AcademicQualificationEntity;
import com.bjet.aki.lms.jpa.TeacherEntity;
import com.bjet.aki.lms.jpa.TrainingEntity;
import com.bjet.aki.lms.repository.AcademicQualificationRepository;
import com.bjet.aki.lms.repository.TrainingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class TeacherMapper {

    private AcademicQualificationRepository academicQualificationRepository;
    private TrainingRepository trainingRepository;
    public ResultMapper<TeacherEntity, Teacher> entityToDomain() {
        return entity -> {
            Teacher teacher = new Teacher();
            teacher.setId(entity.getId());
            teacher.setFirstName(entity.getFirstName());
            teacher.setLastName(entity.getLastName());
            teacher.setEmail(entity.getEmail());
            teacher.setYearsOfExperience(entity.getYearsOfExperience());
            teacher.setAcademicQualifications(entity.getAcademicQualifications()
                    .stream()
                    .map(aqEntity -> academicQualificationEntityToDomain().map(aqEntity))
                    .collect(Collectors.toList())
            );
            teacher.setTrainings(entity.getTrainings()
                    .stream()
                    .map(trainingEntity -> trainingEntityToDomain().map(trainingEntity))
                    .collect(Collectors.toList())
            );
            return teacher;
        };
    }

    public ResultMapper<AcademicQualification, AcademicQualificationEntity> academicQualificationDomainToEntity(){
        return domain -> academicQualificationRepository
                .findById(domain.getId())
                .orElseGet(AcademicQualificationEntity::new)
                .setId(domain.getId())
                .setName(domain.getName())
                .setUniversityName(domain.getUniversityName())
                .setYear(domain.getYear())
                .setResult(domain.getResult());
    }

    public ResultMapper<AcademicQualificationEntity, AcademicQualification> academicQualificationEntityToDomain(){
        return entity -> new AcademicQualification()
                .setId(entity.getId())
                .setName(entity.getName())
                .setUniversityName(entity.getUniversityName())
                .setYear(entity.getYear())
                .setResult(entity.getResult());
    }

    public ResultMapper<Training, TrainingEntity> trainingDomainToEntity(){
        return domain -> trainingRepository
                .findById(domain.getId())
                .orElseGet(TrainingEntity::new)
                .setId(domain.getId())
                .setName(domain.getName())
                .setOrganization(domain.getOrganization())
                .setYear(domain.getYear())
                .setDescription(domain.getDescription());
    }

    public ResultMapper<TrainingEntity, Training> trainingEntityToDomain(){
        return entity -> new Training()
                .setId(entity.getId())
                .setName(entity.getName())
                .setOrganization(entity.getOrganization())
                .setYear(entity.getYear())
                .setDescription(entity.getDescription());
    }
}