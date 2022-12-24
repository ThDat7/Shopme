package com.shopme.common.advice;

import com.shopme.common.exception.InvalidArgumentException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InvalidArgumentExceptionHandler {
    @ExceptionHandler(InvalidArgumentException.class)
    public ErrorResponse invalidArgumentExceptionHandler(InvalidArgumentException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

}
