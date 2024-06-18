package com.bjet.aki.exception;

import static com.bjet.aki.exception.Error.STUDENT_NOT_FOUND_EXCEPTION;

public class StudentNotFoundException extends CommonException{

    public StudentNotFoundException(String message) {
        super(STUDENT_NOT_FOUND_EXCEPTION.getCode(), message);
    }

    public StudentNotFoundException(String message, int weight) {
        super(STUDENT_NOT_FOUND_EXCEPTION.getCode(), message, weight);
    }

    public StudentNotFoundException(String message, int weight, Throwable cause) {
        super(STUDENT_NOT_FOUND_EXCEPTION.getCode(), message, weight, cause);
    }
}
