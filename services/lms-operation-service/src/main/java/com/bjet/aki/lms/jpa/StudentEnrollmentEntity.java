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
@Table(name = "STUDENT_ENROLLMENT")
public class StudentEnrollmentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STUDENT_ENROLLMENT")
    @SequenceGenerator(
            name = "SEQ_STUDENT_ENROLLMENT",
            allocationSize = 1,
            sequenceName = "SEQ_STUDENT_ENROLLMENT")
    private long id;
    private Long courseId;
    private Long studentId;
    private LocalDate enrollmentDate;
}
