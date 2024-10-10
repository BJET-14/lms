package com.bjet.aki.lms.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "EXAM_RESULT")
public class ExamResultEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EXAM_RESULT")
    @SequenceGenerator(
            name = "SEQ_EXAM_RESULT",
            allocationSize = 1,
            sequenceName = "SEQ_EXAM_RESULT")
    private long id;
    private Long examId;
    private Long studentId;
    private Double mark;
    private String comment;
}
