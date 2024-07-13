package com.bjet.aki.lms.service;

import com.bjet.aki.lms.asset.ListResultBuilder;
import com.bjet.aki.lms.asset.PagedResult;
import com.bjet.aki.lms.asset.PagedResultBuilder;
import com.bjet.aki.lms.domain.Teacher;
import com.bjet.aki.lms.jpa.AcademicQualificationEntity;
import com.bjet.aki.lms.jpa.TeacherEntity;
import com.bjet.aki.lms.jpa.TrainingEntity;
import com.bjet.aki.lms.mapper.TeacherMapper;
import com.bjet.aki.lms.repository.AcademicQualificationRepository;
import com.bjet.aki.lms.repository.TeacherRepository;
import com.bjet.aki.lms.repository.TrainingRepository;
import com.bjet.aki.lms.specification.TeacherSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;
    private final AcademicQualificationRepository academicQualificationRepository;
    private final TrainingRepository trainingRepository;


    public PagedResult<Teacher> findAllTeachers(Pageable pageable, String firstName, String lastName, String email) {
        Page<TeacherEntity> teachers = teacherRepository.findAll(TeacherSpecification.findTeachers(firstName, lastName, email), pageable);
        return PagedResultBuilder.build(teachers, teacherMapper.entityToDomain());
    }

    public List<Teacher> findAllTeachers(String firstName, String lastName, String email) {
        List<TeacherEntity> teachers = teacherRepository.findAll(TeacherSpecification.findTeachers(firstName, lastName, email));
        return ListResultBuilder.build(teachers, teacherMapper.entityToDomain());
    }

    public boolean isExist(Long teacherId) {
        return teacherRepository.existsById(teacherId);
    }

    @Transactional
    public void update(Teacher teacher) {
        TeacherEntity teacherEntity = teacherRepository.findById(teacher.getId())
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
        List<TrainingEntity> updatedTrainings = teacher.getTrainings()
                .stream()
                .map(training -> {
                    TrainingEntity trainingEntity = teacherMapper.trainingDomainToEntity().map(training);
                    trainingEntity.setTeacher(teacherEntity);
                    return trainingEntity;
                })
                .collect(Collectors.toList());
        trainingRepository.saveAll(updatedTrainings);

        teacherRepository.save(teacherEntity);
    }
}
