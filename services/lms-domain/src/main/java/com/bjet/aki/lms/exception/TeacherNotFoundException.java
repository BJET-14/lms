package com.bjet.aki.lms.exception;

public class TeacherNotFoundException extends CommonException{

    public TeacherNotFoundException(String message) {
        super(Error.TEACHER_NOT_FOUND_EXCEPTION.getCode(), message);
    }

    public TeacherNotFoundException(String message, int weight) {
        super(Error.TEACHER_NOT_FOUND_EXCEPTION.getCode(), message, weight);
    }

    public TeacherNotFoundException(String message, int weight, Throwable cause) {
        super(Error.TEACHER_NOT_FOUND_EXCEPTION.getCode(), message, weight, cause);
    }
}
