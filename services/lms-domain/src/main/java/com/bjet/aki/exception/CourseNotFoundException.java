package com.bjet.aki.exception;

import static com.bjet.aki.exception.Error.COURSE_NOT_FOUND_EXCEPTION;

public class CourseNotFoundException extends CommonException{

    public CourseNotFoundException(String message) {
        super(COURSE_NOT_FOUND_EXCEPTION.getCode(), message);
    }

    public CourseNotFoundException(String message, int weight) {
        super(COURSE_NOT_FOUND_EXCEPTION.getCode(), message, weight);
    }

    public CourseNotFoundException(String message, int weight, Throwable cause) {
        super(COURSE_NOT_FOUND_EXCEPTION.getCode(), message, weight, cause);
    }
}
