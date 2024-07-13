package com.bjet.aki.lms.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "TEACHER_TRAINING")
public class TrainingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TRAINING")
    @SequenceGenerator(
            name = "SEQ_TRAINING",
            allocationSize = 1,
            sequenceName = "SEQ_TRAINING")
    private long id;
    private String name;
    private String organization;
    private String year;
    private String description;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private TeacherEntity teacher;
}
