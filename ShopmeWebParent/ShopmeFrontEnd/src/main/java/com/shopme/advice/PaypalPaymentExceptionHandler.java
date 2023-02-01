package com.shopme.advice;

import com.shopme.common.advice.ErrorResponse;
import com.shopme.exception.PaypalPaymentException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PaypalPaymentExceptionHandler {
    @ExceptionHandler(PaypalPaymentException.class)
    public ErrorResponse paypalPaymentExceptionHandler(PaypalPaymentException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
