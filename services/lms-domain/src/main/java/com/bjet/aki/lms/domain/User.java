package com.bjet.aki.lms.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class User {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private byte[] profilePicture;
    private Role role;
}
