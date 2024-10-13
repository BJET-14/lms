package com.bjet.aki.lms.jpa;

import com.bjet.aki.lms.model.ExamType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "COURSE_EXAM")
public class ExamEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_COURSE_EXAM")
    @SequenceGenerator(
            name = "SEQ_COURSE_EXAM",
            allocationSize = 1,
            sequenceName = "SEQ_COURSE_EXAM")
    private long id;
    private String name;
    private String description;
    private String googleFormLink;
    private Double fullMarks;
    private Double passMark;
    private ExamType type;
    private Long courseId;
}
