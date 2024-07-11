package com.bjet.aki.lms.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class AcademicQualification {
    private long id;
    private String name;
}
