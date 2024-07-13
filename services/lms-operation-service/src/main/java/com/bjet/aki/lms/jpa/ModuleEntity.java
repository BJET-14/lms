package com.bjet.aki.lms.jpa;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "MODULES")
public class ModuleEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MODULE")
    @SequenceGenerator(
            name = "SEQ_MODULE",
            allocationSize = 1,
            sequenceName = "SEQ_MODULE")
    private long id;
    private String title;
    private String description;
    @NotNull
    @ManyToOne
    private CourseEntity course;
}
