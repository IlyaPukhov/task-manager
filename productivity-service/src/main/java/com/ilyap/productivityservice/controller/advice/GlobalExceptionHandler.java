package com.ilyap.productivityservice.controller.advice;

import com.ilyap.productivityservice.exception.ProductivityAlreadyExistsException;
import com.ilyap.productivityservice.exception.ProductivityNotFoundException;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice(basePackages = "com.ilyap.productivityservice.controller")
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ProductivityNotFoundException.class)
    public Mono<ResponseEntity<ProblemDetail>> handleEntityNotFoundException(ProductivityNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(NOT_FOUND, exception.getMessage());
        return Mono.just(ResponseEntity.status(NOT_FOUND)
                .body(problemDetail));
    }

    @ExceptionHandler(ProductivityAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleEntityAlreadyExistsException(ProductivityAlreadyExistsException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(CONFLICT, exception.getMessage());
        return ResponseEntity.status(CONFLICT)
                .body(problemDetail);
    }
}
