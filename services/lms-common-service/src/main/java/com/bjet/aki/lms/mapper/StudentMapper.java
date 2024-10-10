package com.bjet.aki.lms.mapper;

import com.bjet.aki.lms.asset.ResultMapper;
import com.bjet.aki.lms.jpa.StudentEntity;
import com.bjet.aki.lms.model.Student;
import com.bjet.aki.lms.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class StudentMapper {

    public ResultMapper<StudentEntity, Student> toDomain(){
        return entity -> (Student) new Student()
                .setId(entity.getId())
                .setFirstName(entity.getFirstName())
                .setLastName(entity.getLastName())
                .setEmail(entity.getEmail())
                .setProfilePicture(entity.getProfilePicture());
    }
}
