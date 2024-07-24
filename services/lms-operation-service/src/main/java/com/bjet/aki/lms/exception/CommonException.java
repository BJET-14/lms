package com.bjet.aki.lms.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonException extends RuntimeException{

    private final String code;
    private final int weight;
    private Boolean warning;

    public CommonException(String code, String message) {
        this(code, message, 1, null);
    }

    public CommonException(String code, String message, int weight) {
        this(code, message, weight, null);
    }

    public CommonException(String code, String message, int weight,
                                    Throwable cause) {
        super(message, cause);
        this.code = code;
        this.weight = weight;
    }
}
