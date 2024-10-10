package com.bjet.aki.lms.service;

import com.bjet.aki.lms.model.Student;
import com.bjet.aki.lms.model.Teacher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final RestTemplate restTemplate;

    public Teacher findTeacher(Long teacherId) {
        try{
            Teacher response = restTemplate.getForObject("http://lms-common-service/commons/teachers/" + teacherId, Teacher.class);
            return response;
        } catch (Exception e){
            log.error("Exception occurred in lms-operation-service, Teacher not found. Id={}", teacherId);
            e.printStackTrace();
        }
        return null;
    }

    public Student findStudent(Long studentId) {
        try{
            Student response = restTemplate.getForObject("http://lms-common-service/commons/students/" + studentId, Student.class);
            return response;
        } catch (Exception e){
            log.error("Exception occurred in lms-operation-service, Student not found. Id={}", studentId);
            e.printStackTrace();
        }
        return null;
    }
}
