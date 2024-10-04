package com.bjet.aki.lms.mapper;

import com.bjet.aki.lms.asset.ResultMapper;
import com.bjet.aki.lms.jpa.ProfessionalExperienceEntity;
import com.bjet.aki.lms.model.AcademicQualification;
import com.bjet.aki.lms.model.ProfessionalExperience;
import com.bjet.aki.lms.model.Teacher;
import com.bjet.aki.lms.jpa.AcademicQualificationEntity;
import com.bjet.aki.lms.jpa.TeacherEntity;
import com.bjet.aki.lms.repository.AcademicQualificationRepository;
import com.bjet.aki.lms.repository.ProfessionalExperienceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TeacherMapper {

    private AcademicQualificationRepository academicQualificationRepository;
    private ProfessionalExperienceRepository professionalExperienceRepository;

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
            teacher.setProfessionalExperiences(entity.getProfessionalExperiences()
                    .stream()
                    .map(peEntity -> professionalExperienceEntityToDomain().map(peEntity))
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
                .setName(domain.getName())
                .setPassingYear(domain.getPassingYear())
                .setBoardOrInstitute(domain.getBoardOrInstitute())
                .setResult(domain.getResult());
    }

    public ResultMapper<AcademicQualificationEntity, AcademicQualification> academicQualificationEntityToDomain(){
        return domain -> new AcademicQualification()
                .setId(domain.getId())
                .setName(domain.getName())
                .setPassingYear(domain.getPassingYear())
                .setBoardOrInstitute(domain.getBoardOrInstitute())
                .setResult(domain.getResult());
    }

    public ResultMapper<ProfessionalExperience, ProfessionalExperienceEntity> professionalExperienceDomainToEntity(){
        return domain -> professionalExperienceRepository
                .findById(domain.getId())
                .orElseGet(ProfessionalExperienceEntity::new)
                .setId(domain.getId())
                .setName(domain.getName())
                .setDesignation(domain.getDesignation())
                .setInstitute(domain.getInstitute())
                .setStartTime(domain.getStartTime())
                .setEndTime(domain.getEndTime());
    }

    public ResultMapper<ProfessionalExperienceEntity, ProfessionalExperience> professionalExperienceEntityToDomain(){
        return domain -> new ProfessionalExperience()
                .setId(domain.getId())
                .setName(domain.getName())
                .setDesignation(domain.getDesignation())
                .setInstitute(domain.getInstitute())
                .setStartTime(domain.getStartTime())
                .setEndTime(domain.getEndTime());
    }
}
