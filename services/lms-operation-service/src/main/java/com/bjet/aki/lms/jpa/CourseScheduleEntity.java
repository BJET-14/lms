package com.bjet.aki.lms.jpa;

import com.bjet.aki.lms.model.TimeSlot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.DayOfWeek;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "COURSE_SCHEDULE")
public class CourseScheduleEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_COURSE_SCHEDULE")
    @SequenceGenerator(
            name = "SEQ_COURSE_SCHEDULE",
            allocationSize = 1,
            sequenceName = "SEQ_COURSE_SCHEDULE")
    private long id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private CourseEntity course;

    @Enumerated(EnumType.STRING)
    private DayOfWeek days;

    private TimeSlot timeSlot;
}
