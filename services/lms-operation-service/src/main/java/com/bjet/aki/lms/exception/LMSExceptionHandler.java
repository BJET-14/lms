package com.bjet.aki.lms.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class LMSExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = CommonException.class)
    public @ResponseBody ErrorResponse handleCommonException(CommonException ex) {
        log.error("===================== LMS Common Exception =====================");
        log.error("Common Error occurred in operation service: {}", ex.getMessage());
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public @ResponseBody ErrorResponse handleException(Exception ex) {
        log.error("===================== LMS Exception =====================");
        log.error("Error occurred in operation service: {}", ex.getMessage());
        ex.printStackTrace();
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Severe error occurred in operation service: Contact ADMIN");
    }
}
