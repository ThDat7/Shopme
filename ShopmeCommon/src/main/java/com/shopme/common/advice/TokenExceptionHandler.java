package com.shopme.common.advice;

import com.shopme.common.exception.InvalidJwtTokenException;
import com.shopme.common.exception.TokenRefreshException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TokenExceptionHandler {
    @ExceptionHandler({TokenRefreshException.class, InvalidJwtTokenException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleTokenRefreshException(TokenRefreshException ex) {
        return new ErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }
}
