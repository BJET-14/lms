package com.bjet.aki.lms.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "COURSE_POST")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_COURSE_POST")
    @SequenceGenerator(
            name = "SEQ_COURSE_POST",
            allocationSize = 1,
            sequenceName = "SEQ_COURSE_POST")
    private long id;
    private String postedBy;
    private String message;
    private LocalDateTime postedOn;
    private Long courseId;
}
