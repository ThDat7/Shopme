package com.shopme.exception;

public class ResetPasswordTokenException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "Invalid reset password token";

    public ResetPasswordTokenException() {
        this(DEFAULT_MESSAGE);
    }

    public ResetPasswordTokenException(String message) {
        super(message);
    }
}
