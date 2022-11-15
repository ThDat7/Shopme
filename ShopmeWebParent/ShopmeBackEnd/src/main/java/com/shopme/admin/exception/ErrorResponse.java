package com.shopme.admin.exception;


import org.springframework.http.HttpStatus;

public class ErrorResponse {
    private HttpStatus errorCode;
    private String message;

    public ErrorResponse(HttpStatus errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public HttpStatus getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setErrorCode(HttpStatus errorCode) {
        this.errorCode = errorCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
