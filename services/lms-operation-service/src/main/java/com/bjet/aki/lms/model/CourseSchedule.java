package com.bjet.aki.lms.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CourseSchedule {
    private long id;
    private WeeklyDays days;
    private TimeSlot timeSlot;
}
