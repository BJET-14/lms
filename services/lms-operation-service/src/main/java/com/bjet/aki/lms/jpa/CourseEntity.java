package com.bjet.aki.lms.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "COURSES")
public class CourseEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_COURSE")
    @SequenceGenerator(
            name = "SEQ_COURSE",
            allocationSize = 1,
            sequenceName = "SEQ_COURSE")
    private long id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isComplete = Boolean.FALSE;
    @OneToMany(mappedBy = "course")
    private List<ModuleEntity> modules;
    private String classMeetingLink;
    private Long teacherId;
}
