package com.ilyap.taskmanager.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.ilyap.taskmanager.controller")
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    ProblemDetail handleTaskNotFoundException(Exception exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }
}
