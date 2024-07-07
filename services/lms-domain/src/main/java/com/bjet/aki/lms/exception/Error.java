package com.bjet.aki.lms.exception;

import lombok.Getter;

@Getter
public enum Error {
    USER_NOT_FOUND_EXCEPTION("001"),
    COURSE_NOT_FOUND_EXCEPTION("002"),
    TEACHER_NOT_FOUND_EXCEPTION("003"),
    STUDENT_NOT_FOUND_EXCEPTION("004"),
    ;
    // region <R>
    Error(String code) {
        this.code = code;
    }

    private String code;

    public void setCode(String code) {
        this.code = code;
    }

}
