package com.shopme.exception;

public class CartItemOutOfQuantityException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "Out of maximum quantity for buying";

    public CartItemOutOfQuantityException() {
        this(DEFAULT_MESSAGE);
    }

    public CartItemOutOfQuantityException(String message) {
        super(message);
    }
}
