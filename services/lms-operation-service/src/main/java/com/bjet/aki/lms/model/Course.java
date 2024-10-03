package com.bjet.aki.lms.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class Course {
    private long id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isComplete;
    private List<Module> modules;
    private List<WeeklyDays> days;
    private String classMeetingLink;
    private Long teacherId;
}
