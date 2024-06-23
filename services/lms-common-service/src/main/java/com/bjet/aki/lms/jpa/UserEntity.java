package com.bjet.aki.lms.jpa;

import com.bjet.aki.lms.domain.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "USER_TYPE", discriminatorType = DiscriminatorType.STRING)
@Table(name = "USERS")
public class UserEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USER")
    private long id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private byte[] profilePicture;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "PASS_RESET_REQUIRE")
    private boolean passwordResetRequired;
}
