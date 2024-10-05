package com.bjet.aki.lms.jpa;

import com.bjet.aki.lms.model.TimeSlot;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "CLASS_SCHEDULE")
@NoArgsConstructor
@AllArgsConstructor
public class ClassScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CLASS_SCHEDULE")
    @SequenceGenerator(
            name = "SEQ_CLASS_SCHEDULE",
            allocationSize = 1,
            sequenceName = "SEQ_CLASS_SCHEDULE")
    private long id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private CourseEntity course;

    private LocalDateTime classDateTime;
    private TimeSlot timeSlot;
}
