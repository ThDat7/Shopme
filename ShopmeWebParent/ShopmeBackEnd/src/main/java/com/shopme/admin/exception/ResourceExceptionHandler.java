package com.shopme.admin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ResourceExceptionHandler {
    @ExceptionHandler(ResourceAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handlerResourceAlreadyExist(ResourceAlreadyExistException ex ) {
        return new ErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }
}
