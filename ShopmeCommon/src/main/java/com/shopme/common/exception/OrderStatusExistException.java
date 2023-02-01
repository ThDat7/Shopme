package com.shopme.common.exception;

public class OrderStatusExistException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Order status has been exist";

    public OrderStatusExistException() {
        this(DEFAULT_MESSAGE);
    }

    public OrderStatusExistException(String message) {
        super(message);
    }
}
