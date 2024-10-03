package com.bjet.aki.lms.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class Teacher extends User{
    private List<AcademicQualification> academicQualifications;
    private List<ProfessionalExperience> professionalExperiences;
    private Double yearsOfExperience;
}
