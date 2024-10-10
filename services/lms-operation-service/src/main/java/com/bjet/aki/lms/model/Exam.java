package com.bjet.aki.lms.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class Exam {
    private long id;
    private String name;
    private String description;
    private String googleFormLink;
    private Double fullMark;
    private Double passMark;
    private ExamType type;
}
