package com.bjet.aki.lms.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class Post {
    private long id;
    private String postedBy;
    private String message;
    private LocalDateTime postedOn;
}
