package com.shopme.common.exception;

public class ResourceAlreadyExistException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "Resource already exists.";

    public ResourceAlreadyExistException() {
        this(DEFAULT_MESSAGE);
    }

    public ResourceAlreadyExistException(String message) {
        super(message);
    }

}
