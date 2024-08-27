package com.ilyap.userservice.controller.advice;

import com.ilyap.userservice.exception.UserAlreadyExistsException;
import com.ilyap.userservice.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Global exception handler for the user service.
 * <p>
 * This class provides a centralized way to handle exceptions that occur in the user service.
 * It uses Spring's {@link RestControllerAdvice @RestControllerAdvice} annotation to catch exceptions thrown by controllers.
 */
@RestControllerAdvice(basePackages = "com.ilyap.userservice.controller")
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles {@link UserNotFoundException} exceptions.
     * <p>
     * This method catches UserNotFoundException exceptions thrown by controllers and returns
     * a ResponseEntity with a ProblemDetail object containing the exception message and a
     * {@link HttpStatus#NOT_FOUND NOT_FOUND} status code.
     *
     * @param exception the {@link UserNotFoundException} exception to handle
     * @return a {@link ResponseEntity} with a {@link ProblemDetail} object and
     * a {@link HttpStatus#NOT_FOUND NOT_FOUND} status code
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleEntityNotFoundException(UserNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(NOT_FOUND)
                .body(problemDetail);
    }

    /**
     * Handles {@link UserAlreadyExistsException} exceptions.
     * <p>
     * This method catches UserAlreadyExistsException exceptions thrown by controllers and returns
     * a ResponseEntity with a ProblemDetail object containing the exception message and a
     * {@link HttpStatus#CONFLICT CONFLICT} status code.
     *
     * @param exception the {@link UserAlreadyExistsException} exception to handle
     * @return a {@link ResponseEntity} with a {@link ProblemDetail} object and
     * a {@link HttpStatus#CONFLICT CONFLICT} status code
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleEntityAlreadyExistsException(UserAlreadyExistsException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(CONFLICT, exception.getMessage());
        return ResponseEntity.status(CONFLICT)
                .body(problemDetail);
    }
}
