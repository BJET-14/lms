package com.bjet.aki.lms.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class FileManagement {
    private long id;
    private String name;
    private FileType type;
    private String url;
    private Long courseId;
    private Long moduleId;
}
