package com.bjet.aki.lms.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
public class ProfessionalExperience {
    private long id;
    private String name;
    private String institute;
    private LocalDate startTime;
    private LocalDate endTime;
    private String designation;
}
