package com.shopme.common.exception;

public class ResourceNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Resource not found";

    public ResourceNotFoundException() {
        this(DEFAULT_MESSAGE);
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
