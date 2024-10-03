package com.bjet.aki.lms.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "PROFESSIONAL_EXPERIENCE")
public class ProfessionalExperienceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PROFESSIONAL_EXPERIENCE")
    @SequenceGenerator(
            name = "SEQ_PROFESSIONAL_EXPERIENCE",
            allocationSize = 1,
            sequenceName = "SEQ_PROFESSIONAL_EXPERIENCE")
    private long id;
    private String name;
    private String institute;
    private LocalDate startTime;
    private LocalDate endTime;
    private String designation;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private TeacherEntity teacher;
}
