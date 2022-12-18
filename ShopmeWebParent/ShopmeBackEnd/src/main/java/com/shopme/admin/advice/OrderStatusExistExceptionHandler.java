package com.shopme.admin.advice;

import com.shopme.admin.exception.OrderStatusExistException;
import com.shopme.common.advice.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderStatusExistExceptionHandler {
    @ExceptionHandler(OrderStatusExistException.class)
    public ErrorResponse orderStatusExistExceptionHandler(OrderStatusExistException e) {
        return new ErrorResponse(HttpStatus.CONFLICT, e.getMessage());
    }
}
