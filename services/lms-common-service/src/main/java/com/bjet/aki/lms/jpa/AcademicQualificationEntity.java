package com.bjet.aki.lms.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "TEACHER_ACADEMIC_QUALIFICATION")
public class AcademicQualificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ACAD_QUALIFICATION")
    @SequenceGenerator(
            name = "SEQ_ACAD_QUALIFICATION",
            allocationSize = 1,
            sequenceName = "SEQ_ACAD_QUALIFICATION")
    private long id;
    private String name;
    private String universityName; // universityName
    private String year;
    private Double result;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private TeacherEntity teacher;
}
