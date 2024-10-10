package com.bjet.aki.lms.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class DetailClassSchedule {
    private  long id;
    private LocalDate date;
    private String formattedDate;
    private LocalTime time;
    private String title;
}
