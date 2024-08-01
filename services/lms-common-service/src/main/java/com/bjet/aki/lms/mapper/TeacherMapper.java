package com.bjet.aki.lms.mapper;

import com.bjet.aki.lms.asset.ResultMapper;
import com.bjet.aki.lms.model.AcademicQualification;
import com.bjet.aki.lms.model.Teacher;
import com.bjet.aki.lms.jpa.AcademicQualificationEntity;
import com.bjet.aki.lms.jpa.TeacherEntity;
import com.bjet.aki.lms.repository.AcademicQualificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TeacherMapper {

    private AcademicQualificationRepository academicQualificationRepository;

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
                    .toList()
            );
            return teacher;
        };
    }

    public ResultMapper<AcademicQualification, AcademicQualificationEntity> academicQualificationDomainToEntity(){
        return domain -> academicQualificationRepository
                .findById(domain.getId())
                .orElseGet(AcademicQualificationEntity::new)
                .setId(domain.getId())
                .setName(domain.getName());
    }

    public ResultMapper<AcademicQualificationEntity, AcademicQualification> academicQualificationEntityToDomain(){
        return domain -> new AcademicQualification()
                .setId(domain.getId())
                .setName(domain.getName());
    }
}
