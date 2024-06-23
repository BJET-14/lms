package com.bjet.aki.lms.mapper;

import com.bjet.aki.lms.asset.ResultMapper;
import com.bjet.aki.lms.domain.User;
import com.bjet.aki.lms.jpa.AdminEntity;
import com.bjet.aki.lms.jpa.StudentEntity;
import com.bjet.aki.lms.jpa.TeacherEntity;
import com.bjet.aki.lms.jpa.UserEntity;
import com.bjet.aki.lms.repository.AdminRepository;
import com.bjet.aki.lms.repository.StudentRepository;
import com.bjet.aki.lms.repository.TeacherRepository;
import com.bjet.aki.lms.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMapper {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final AdminRepository adminRepository;

    public ResultMapper<UserEntity, User> toUserDomain(boolean passwordRequired){
        return entity -> new User()
                .setId(entity.getId())
                .setEmail(entity.getEmail())
                .setFirstName(entity.getFirstName())
                .setLastName(entity.getLastName())
                .setRole(entity.getRole())
                .setPassword(passwordRequired ? entity.getPassword() : null);
    }

    public ResultMapper<User, StudentEntity> toStudentEntity(){
        return domain -> (StudentEntity) userRepository.findByEmail(domain.getEmail())
                .orElseGet(StudentEntity::new)
                .setEmail(domain.getEmail())
                .setFirstName(domain.getFirstName())
                .setLastName(domain.getLastName())
                .setPassword(domain.getPassword());
    }

    public ResultMapper<User, AdminEntity> toAdminEntity(){
        return domain -> (AdminEntity) userRepository.findByEmail(domain.getEmail())
                .orElseGet(AdminEntity::new)
                .setEmail(domain.getEmail())
                .setFirstName(domain.getFirstName())
                .setLastName(domain.getLastName())
                .setPassword(domain.getPassword());
    }

    public ResultMapper<User, TeacherEntity> toTeacherEntity(){
        return domain -> (TeacherEntity) userRepository.findByEmail(domain.getEmail())
                .orElseGet(TeacherEntity::new)
                .setEmail(domain.getEmail())
                .setFirstName(domain.getFirstName())
                .setLastName(domain.getLastName())
                .setPassword(domain.getPassword());
    }
}
