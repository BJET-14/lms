package com.bjet.aki.lms.jpa;

import com.bjet.aki.lms.model.FileType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "COURSE_FILE")
public class FileManagementEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_COURSE_FILE")
    @SequenceGenerator(
            name = "SEQ_COURSE_FILE",
            allocationSize = 1,
            sequenceName = "SEQ_COURSE_FILE")
    private long id;
    private String name;
    private FileType type;
    private String url;
    private Long courseId;
    private Long moduleId;
}
