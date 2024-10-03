package com.bjet.aki.lms.service;

import com.bjet.aki.lms.asset.ListResultBuilder;
import com.bjet.aki.lms.asset.PagedResult;
import com.bjet.aki.lms.asset.PagedResultBuilder;
import com.bjet.aki.lms.exceptions.CommonException;
import com.bjet.aki.lms.jpa.AcademicQualificationEntity;
import com.bjet.aki.lms.jpa.ProfessionalExperienceEntity;
import com.bjet.aki.lms.model.Teacher;
import com.bjet.aki.lms.jpa.TeacherEntity;
import com.bjet.aki.lms.mapper.TeacherMapper;
import com.bjet.aki.lms.repository.AcademicQualificationRepository;
import com.bjet.aki.lms.repository.ProfessionalExperienceRepository;
import com.bjet.aki.lms.repository.TeacherRepository;
import com.bjet.aki.lms.specification.TeacherSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final AcademicQualificationRepository academicQualificationRepository;
    private final ProfessionalExperienceRepository professionalExperienceRepository;
    private final TeacherMapper teacherMapper;

    public PagedResult<Teacher> findAllTeachers(Pageable pageable, String firstName, String lastName, String email) {
        Page<TeacherEntity> teachers = teacherRepository.findAll(TeacherSpecification.findTeachers(firstName, lastName, email), pageable);
        return PagedResultBuilder.build(teachers, teacherMapper.entityToDomain());
    }

    public List<Teacher> findAllTeachers(String firstName, String lastName, String email) {
        List<TeacherEntity> teachers = teacherRepository.findAll(TeacherSpecification.findTeachers(firstName, lastName, email));
        return ListResultBuilder.build(teachers, teacherMapper.entityToDomain());
    }

    public boolean isExist(Long teacherId) {
        return teacherRepository.findById(teacherId).isPresent();
    }

    public void update(Long teacherId, Teacher teacher) {
        TeacherEntity teacherEntity = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        // Update teacher's basic info
        teacherEntity.setYearsOfExperience(teacher.getYearsOfExperience());

        // Update academic qualifications
        List<AcademicQualificationEntity> updatedQualifications = teacher.getAcademicQualifications()
                .stream()
                .map(qualification -> {
                    AcademicQualificationEntity qualificationEntity = teacherMapper.academicQualificationDomainToEntity().map(qualification);
                    qualificationEntity.setTeacher(teacherEntity);
                    return qualificationEntity;
                })
                .collect(Collectors.toList());
        academicQualificationRepository.saveAll(updatedQualifications);

        // Update trainings
        List<ProfessionalExperienceEntity> professionalExperiences = teacher.getProfessionalExperiences()
                .stream()
                .map(experience -> {
                    ProfessionalExperienceEntity experienceEntity = teacherMapper.professionalExperienceDomainToEntity().map(experience);
                    experienceEntity.setTeacher(teacherEntity);
                    return experienceEntity;
                })
                .collect(Collectors.toList());
        professionalExperienceRepository.saveAll(professionalExperiences);

        teacherRepository.save(teacherEntity);
    }

    public Teacher getTeacher(Long teacherId) {
        log.info("Fetching teacher. Id: {}", teacherId);
        TeacherEntity entity = teacherRepository.findById(teacherId).orElseThrow(() -> new CommonException("Teacher not found. Id: {}", teacherId.toString()));
        return teacherMapper.entityToDomain().map(entity);
    }
}
