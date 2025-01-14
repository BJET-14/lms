package com.bjet.aki.lms.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.DayOfWeek;

@Getter
@Setter
@Accessors(chain = true)
public class CourseSchedule {
    private long id;
    private DayOfWeek days;
    private TimeSlot timeSlot;
}
