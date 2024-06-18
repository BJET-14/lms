package com.bjet.aki.exception;

import static com.bjet.aki.exception.Error.TEACHER_NOT_FOUND_EXCEPTION;

public class TeacherNotFoundException extends CommonException{

    public TeacherNotFoundException(String message) {
        super(TEACHER_NOT_FOUND_EXCEPTION.getCode(), message);
    }

    public TeacherNotFoundException(String message, int weight) {
        super(TEACHER_NOT_FOUND_EXCEPTION.getCode(), message, weight);
    }

    public TeacherNotFoundException(String message, int weight, Throwable cause) {
        super(TEACHER_NOT_FOUND_EXCEPTION.getCode(), message, weight, cause);
    }
}
