package com.bjet.aki.lms.service;

import com.bjet.aki.lms.domain.User;
import com.bjet.aki.lms.jpa.UserEntity;
import com.bjet.aki.lms.mapper.UserMapper;
import com.bjet.aki.lms.repository.AdminRepository;
import com.bjet.aki.lms.repository.StudentRepository;
import com.bjet.aki.lms.repository.TeacherRepository;
import com.bjet.aki.lms.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;
    private final TeacherRepository teacherRepository;

    public void saveUser(User user){
        logger.info("Request to save user. email={}", user.getEmail());
        switch (user.getRole()){
            case ADMIN -> {
                var adminUser = userMapper.toAdminEntity().map(user);
                adminUser.setPasswordResetRequired(true);
                adminRepository.save(adminUser);
            }
            case TEACHER ->{
                var teacherUser = userMapper.toTeacherEntity().map(user);
                teacherUser.setPasswordResetRequired(true);
                teacherRepository.save(teacherUser);
            }
            case STUDENT ->{
                var studentUser = userMapper.toStudentEntity().map(user);
                studentUser.setPasswordResetRequired(true);
                studentRepository.save(studentUser);
            }
            default -> throw new IllegalStateException("Unexpected value: " + user.getRole());
        }
    }

    public User findByEmail(String email) {
        logger.info("Request to fetch user for auth purpose. email={}", email);
        UserEntity entity = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not exist"));
        return userMapper.toUserDomain(true).map(entity);
    }

    public Boolean exist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
