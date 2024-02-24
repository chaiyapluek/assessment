package com.kbtg.bootcamp.posttest.exception;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class ApiExceptionHandler {
    
    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ApiExceptionResponse> handleNotFoundException(NotFoundException e) {
        ApiExceptionResponse response = new ApiExceptionResponse(e.getMessage(), ZonedDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ApiExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String field = e.getBindingResult().getFieldError().getField();
        String err = e.getBindingResult().getFieldError().getDefaultMessage();
        String msg = String.format("%s %s", field, err);
        ApiExceptionResponse response = new ApiExceptionResponse(msg, ZonedDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = {DuplicateException.class})
    public ResponseEntity<ApiExceptionResponse> handleDuplicateException(DuplicateException e) {
        ApiExceptionResponse response = new ApiExceptionResponse(e.getMessage(), ZonedDateTime.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<ApiExceptionResponse> handleConstraintViolationException(ConstraintViolationException e) {
        String errMsg = e.getConstraintViolations().stream().findFirst().map(v -> {
            String[] arr = v.getPropertyPath().toString().split("\\.");
            return arr[arr.length - 1] + " " + v.getMessage();
        }).orElse("invalid input");
        ApiExceptionResponse response = new ApiExceptionResponse(errMsg, ZonedDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = {InsufficientLotteryException.class})
    public ResponseEntity<ApiExceptionResponse> handleInsufficientLotteryException(InsufficientLotteryException e) {
        ApiExceptionResponse response = new ApiExceptionResponse(e.getMessage(), ZonedDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // @ExceptionHandler(value = {Exception.class})
    // public ResponseEntity<ApiExceptionResponse> handleException(Exception e) {
    //     System.out.println(e.getMessage());
    //     ApiExceptionResponse response = new ApiExceptionResponse("internal server error", ZonedDateTime.now());
    //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    // }

    // @ExceptionHandler(value = {RuntimeException.class})
    // public ResponseEntity<ApiExceptionResponse> handleRuntimeException(RuntimeException e) {
    //     System.out.println(e.getMessage());
    //     ApiExceptionResponse response = new ApiExceptionResponse("internal server error", ZonedDateTime.now());
    //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    // }
}
