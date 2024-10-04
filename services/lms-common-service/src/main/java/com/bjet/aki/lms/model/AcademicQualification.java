package com.bjet.aki.lms.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
public class AcademicQualification {
    private long id;
    private String name;
    private Integer passingYear;
    private Double result;
    private String boardOrInstitute;
}
