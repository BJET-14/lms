package com.bjet.aki.lms.service;

import com.bjet.aki.lms.exceptions.CommonException;
import com.bjet.aki.lms.jpa.StudentEntity;
import com.bjet.aki.lms.mapper.StudentMapper;
import com.bjet.aki.lms.model.Student;
import com.bjet.aki.lms.repository.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public boolean isExist(Long studentId) {
        return studentRepository.existsById(studentId);
    }

    public Student getStudent(Long studentId) {
        StudentEntity student = studentRepository.findById(studentId)
                .orElseThrow(() -> new CommonException("06", "Student not found"));
        return studentMapper.toDomain().map(student);
    }
}
