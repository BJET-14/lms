package com.bjet.aki.lms.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class RegistrationSuccessNotificationRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String subject = "Registration Completed!!!";
}
