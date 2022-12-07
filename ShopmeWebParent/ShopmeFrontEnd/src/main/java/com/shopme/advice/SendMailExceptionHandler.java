package com.shopme.advice;

import com.shopme.common.advice.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@RestControllerAdvice
public class SendMailExceptionHandler {
    @ExceptionHandler({MessagingException.class, UnsupportedEncodingException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleSendMailException(Exception e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
