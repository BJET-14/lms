package com.bjet.aki.lms.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ExamResult {
    private Long id;
    private Long examId;
    private Long studentId;
    private Double mark;
    private String comment;
}
