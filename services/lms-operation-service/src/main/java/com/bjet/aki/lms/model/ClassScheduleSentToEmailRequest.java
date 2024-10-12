package com.bjet.aki.lms.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class ClassScheduleSentToEmailRequest {
    private String receiverEmailAddress;
    private String receiverName;
    private String teacherName;
    private String courseTitle;
    private String classMeetingLink;
    private List<DetailClassSchedule> classSchedules;
    private boolean isSendingToStudent;
}
