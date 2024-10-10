package com.bjet.aki.lms.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class ExamResultDetails {
    private Long examId;
    private String examName;
    private ExamType examType;
    private Double fullMark;
    private Double highestMark;
    private Double lowestMark;
    private List<ExamResult> results;
}
