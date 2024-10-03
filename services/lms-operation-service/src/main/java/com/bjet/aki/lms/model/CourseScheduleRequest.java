package com.bjet.aki.lms.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class CourseScheduleRequest {
    private Long courseId;
    private List<CourseSchedule> schedules;
}
