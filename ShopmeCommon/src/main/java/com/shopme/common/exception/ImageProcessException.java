package com.shopme.common.exception;

public class ImageProcessException extends RuntimeException{
    private static final
        String DEFAULT_MESSAGE = "An error occurred while processing the image.";

    public ImageProcessException() {
        this(DEFAULT_MESSAGE);
    }

    public ImageProcessException(String message) {
        super(message);
    }
}
