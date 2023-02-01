package com.shopme.common.exception;

public class InvalidArgumentException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Invalid argument exception";

    public InvalidArgumentException() {
        this(DEFAULT_MESSAGE);
    }

    public InvalidArgumentException(String message) {
        super(message);
    }
}
