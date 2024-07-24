package com.bjet.aki.lms.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserSaveRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String encryptedPassword;
    private byte[] profilePicture;
    private Role role;
}
