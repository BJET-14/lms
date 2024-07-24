package com.bjet.aki.lms.jpa;

import com.bjet.aki.lms.model.Role;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@DiscriminatorValue("S")
public class StudentEntity extends UserEntity{
    public StudentEntity() {
        this.setRole(Role.STUDENT);
    }
}
