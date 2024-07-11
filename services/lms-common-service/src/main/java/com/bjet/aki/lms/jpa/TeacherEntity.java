package com.bjet.aki.lms.jpa;

import com.bjet.aki.lms.domain.Role;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@DiscriminatorValue("T")
public class TeacherEntity extends UserEntity{
    public TeacherEntity() {
        this.setRole(Role.TEACHER);
    }

    @Column(name = "TOTAL_EXPERIENCE")
    private Double yearsOfExperience;

    @OneToMany(mappedBy = "teacher")
    private List<AcademicQualificationEntity> academicQualifications;
}
