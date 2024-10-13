package com.bjet.aki.lms.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ExamLinkSentToStudentRequest {
    private String receiverEmail;
    private String subject;
    private String studentName;
    private String examName;
    private String examLink;
    private Double fullMark;
    private Double passedMark;
}
