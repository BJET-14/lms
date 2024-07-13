package com.bjet.aki.lms.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class Teacher extends User{
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<AcademicQualification> academicQualifications;
    private Double yearsOfExperience;
    private List<Training> trainings;
}
