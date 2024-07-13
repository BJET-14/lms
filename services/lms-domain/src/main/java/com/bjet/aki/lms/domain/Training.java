package com.bjet.aki.lms.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class Training {
    private long id;
    private String name;
    private String organization;
    private String year;
    private String description;
}
