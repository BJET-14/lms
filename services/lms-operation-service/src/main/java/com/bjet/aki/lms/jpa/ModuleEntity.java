package com.bjet.aki.lms.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "COuRSE_MODULES")
public class ModuleEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_COURSE_MODULE")
    @SequenceGenerator(
            name = "SEQ_COURSE_MODULE",
            allocationSize = 1,
            sequenceName = "SEQ_COURSE_MODULE")
    private long id;
    private String title;
    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column
    private String description;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private CourseEntity course;
}
