package com.shopme.common.exception;

public class InvalidJwtTokenException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "Invalid JWT";

    public InvalidJwtTokenException() {
        this(DEFAULT_MESSAGE);
    }

    public InvalidJwtTokenException(String message) {
        super(message);
    }
}
