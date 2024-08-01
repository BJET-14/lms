package com.bjet.aki.lms.service;

import com.bjet.aki.lms.asset.ListResultBuilder;
import com.bjet.aki.lms.asset.PagedResult;
import com.bjet.aki.lms.asset.PagedResultBuilder;
import com.bjet.aki.lms.model.Teacher;
import com.bjet.aki.lms.jpa.TeacherEntity;
import com.bjet.aki.lms.mapper.TeacherMapper;
import com.bjet.aki.lms.repository.TeacherRepository;
import com.bjet.aki.lms.specification.TeacherSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
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
        return false;
    }

    public void update(Teacher teacher) {

    }
}
