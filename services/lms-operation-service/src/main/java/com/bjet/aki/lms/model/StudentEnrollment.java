package com.bjet.aki.lms.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
public class StudentEnrollment {
    private long id;
    private Long courseId;
    private String courseName;
    private Long studentId;
    private String studentName;
    private LocalDate enrollmentDate;
}
