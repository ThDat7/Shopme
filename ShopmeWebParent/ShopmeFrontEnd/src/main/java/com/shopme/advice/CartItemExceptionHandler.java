package com.shopme.advice;

import com.shopme.common.advice.ErrorResponse;
import com.shopme.exception.CartItemOutOfQuantityException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CartItemExceptionHandler {
    @ExceptionHandler(CartItemOutOfQuantityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse cartItemExceptionHandler(CartItemOutOfQuantityException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
