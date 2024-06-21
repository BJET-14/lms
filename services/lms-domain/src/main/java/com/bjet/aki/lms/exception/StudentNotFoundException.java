package com.bjet.aki.lms.exception;

public class StudentNotFoundException extends CommonException{

    public StudentNotFoundException(String message) {
        super(Error.STUDENT_NOT_FOUND_EXCEPTION.getCode(), message);
    }

    public StudentNotFoundException(String message, int weight) {
        super(Error.STUDENT_NOT_FOUND_EXCEPTION.getCode(), message, weight);
    }

    public StudentNotFoundException(String message, int weight, Throwable cause) {
        super(Error.STUDENT_NOT_FOUND_EXCEPTION.getCode(), message, weight, cause);
    }
}
