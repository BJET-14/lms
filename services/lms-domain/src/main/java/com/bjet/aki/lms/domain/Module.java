package com.bjet.aki.lms.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


@Getter
@Setter
@Accessors(chain = true)
public class Module {
    private long id;
    private String title;
    private String description;
    @JsonProperty(value = "course_id")
    private long courseId;
}
