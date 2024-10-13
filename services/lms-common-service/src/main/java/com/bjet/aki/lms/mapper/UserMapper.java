package com.bjet.aki.lms.mapper;

import com.bjet.aki.lms.asset.ResultMapper;
import com.bjet.aki.lms.model.RegistrationSuccessNotificationRequest;
import com.bjet.aki.lms.model.User;
import com.bjet.aki.lms.model.UserSaveRequest;
import com.bjet.aki.lms.jpa.AdminEntity;
import com.bjet.aki.lms.jpa.StudentEntity;
import com.bjet.aki.lms.jpa.TeacherEntity;
import com.bjet.aki.lms.jpa.UserEntity;
import com.bjet.aki.lms.repository.AdminRepository;
import com.bjet.aki.lms.repository.StudentRepository;
import com.bjet.aki.lms.repository.TeacherRepository;
import com.bjet.aki.lms.repository.UserRepository;
import com.bjet.aki.lms.util.ImageUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMapper {

    private final UserRepository userRepository;

    public ResultMapper<UserEntity, User> toUserDomain(boolean passwordRequired, boolean imageRequired) {
        return entity -> new User()
                .setId(entity.getId())
                .setEmail(entity.getEmail())
                .setFirstName(entity.getFirstName())
                .setLastName(entity.getLastName())
                .setRole(entity.getRole())
                .setProfilePicture(imageRequired && entity.getProfilePicture() != null ? ImageUtils.decompressImage(entity.getProfilePicture()) : null)
                .setPassword(passwordRequired ? entity.getPassword() : null);
    }

    public ResultMapper<UserSaveRequest, StudentEntity> toStudentEntity(){
        return domain -> (StudentEntity) userRepository.findByEmail(domain.getEmail())
                .orElseGet(StudentEntity::new)
                .setEmail(domain.getEmail())
                .setFirstName(domain.getFirstName())
                .setLastName(domain.getLastName())
                .setPassword(domain.getEncryptedPassword());
    }

    public ResultMapper<UserSaveRequest, AdminEntity> toAdminEntity(){
        return domain -> (AdminEntity) userRepository.findByEmail(domain.getEmail())
                .orElseGet(AdminEntity::new)
                .setEmail(domain.getEmail())
                .setFirstName(domain.getFirstName())
                .setLastName(domain.getLastName())
                .setPassword(domain.getEncryptedPassword());
    }

    public ResultMapper<UserSaveRequest, TeacherEntity> toTeacherEntity(){
        return domain -> (TeacherEntity) userRepository.findByEmail(domain.getEmail())
                .orElseGet(TeacherEntity::new)
                .setEmail(domain.getEmail())
                .setFirstName(domain.getFirstName())
                .setLastName(domain.getLastName())
                .setPassword(domain.getEncryptedPassword());
    }

    public ResultMapper<UserSaveRequest, RegistrationSuccessNotificationRequest> toNotificationRequest() {
        return domain -> new RegistrationSuccessNotificationRequest()
                .setFirstName(domain.getFirstName())
                .setLastName(domain.getLastName())
                .setEmail(domain.getEmail())
                .setPassword(domain.getPassword());
    }
}
