package com.bjet.aki.lms.service;

import com.bjet.aki.lms.asset.ListResultBuilder;
import com.bjet.aki.lms.asset.PagedResult;
import com.bjet.aki.lms.asset.PagedResultBuilder;
import com.bjet.aki.lms.exceptions.CommonException;
import com.bjet.aki.lms.jpa.AdminEntity;
import com.bjet.aki.lms.jpa.StudentEntity;
import com.bjet.aki.lms.jpa.TeacherEntity;
import com.bjet.aki.lms.model.Role;
import com.bjet.aki.lms.model.User;
import com.bjet.aki.lms.model.UserSaveRequest;
import com.bjet.aki.lms.jpa.UserEntity;
import com.bjet.aki.lms.mapper.UserMapper;
import com.bjet.aki.lms.repository.AdminRepository;
import com.bjet.aki.lms.repository.StudentRepository;
import com.bjet.aki.lms.repository.TeacherRepository;
import com.bjet.aki.lms.repository.UserRepository;
import com.bjet.aki.lms.specification.UserSpecification;
import com.bjet.aki.lms.util.ImageUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;
    private final TeacherRepository teacherRepository;
    private final EmailNotificationService notificationService;

    public Long saveUser(UserSaveRequest user){
        logger.info("Request to save user. email={}", user.getEmail());
        switch (user.getRole()){
            case ADMIN -> {
                var adminUser = userMapper.toAdminEntity().map(user);
                adminUser.setPasswordResetRequired(true);
                AdminEntity admin = adminRepository.save(adminUser);
                notificationService.sendEmailNotificationForRegistration(userMapper.toNotificationRequest().map(user));
                return admin.getId();
            }
            case TEACHER ->{
                var teacherUser = userMapper.toTeacherEntity().map(user);
                teacherUser.setPasswordResetRequired(true);
                TeacherEntity teacher = teacherRepository.save(teacherUser);
                notificationService.sendEmailNotificationForRegistration(userMapper.toNotificationRequest().map(user));
                return teacher.getId();
            }
            case STUDENT ->{
                var studentUser = userMapper.toStudentEntity().map(user);
                studentUser.setPasswordResetRequired(true);
                StudentEntity student = studentRepository.save(studentUser);
                notificationService.sendEmailNotificationForRegistration(userMapper.toNotificationRequest().map(user));
                return student.getId();
            }
            default -> throw new IllegalStateException("Unexpected value: " + user.getRole());
        }
    }

    public User findByEmail(String email) {
        logger.info("Request to fetch user for auth purpose. email={}", email);
        UserEntity entity = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not exist"));
        return userMapper.toUserDomain(true, true).map(entity);
    }

    public Boolean exist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public PagedResult<User> findAllUsers(Pageable pageable, String firstName, String lastName, String email, Role role) {
        Page<UserEntity> users = userRepository.findAll(UserSpecification.findUsers(firstName, lastName, email, role), pageable);
        return PagedResultBuilder.build(users, userMapper.toUserDomain(false, false));
    }

    public List<User> findAllUsers(String firstName, String lastName, String email, Role role) {
        List<UserEntity> users = userRepository.findAll(UserSpecification.findUsers(firstName, lastName, email, role));
        return ListResultBuilder.build(users, userMapper.toUserDomain(false, false));
    }

    public User findById(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new CommonException("00", "User not found"));
        return userMapper.toUserDomain(false, true).map(userEntity);
    }

    public boolean isExist(Long id){
        return userRepository.findById(id).isPresent();
    }

    public void uploadProfilePicture(Long id, MultipartFile file) {
        validateMultipartFile(file);
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new CommonException("00", "User not found"));
        try {
            byte[] image = ImageUtils.compressImage(file.getBytes());
            user.setProfilePicture(image);
            userRepository.save(user);
        } catch (IOException e) {
            throw new CommonException("11", "Exception occurred in uploading profile picture");
        }
    }

    private void validateMultipartFile(MultipartFile file) {
        long maxSize = 256 * 256;  // 256 KB
        if (file.getSize() > maxSize) {
            throw new CommonException("09", "File size exceeds limit of 256 KB");
        }

        String contentType = file.getContentType();
        List<String> allowedTypes = Arrays.asList("image/jpeg", "image/png");
        if (!allowedTypes.contains(contentType)) {
            throw new CommonException("10", "Only JPG and PNG images are allowed");
        }
    }
}
