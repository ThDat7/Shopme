package com.shopme.exception;

public class PaypalPaymentException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Error when process paypal.";

    public PaypalPaymentException() {
        this(DEFAULT_MESSAGE);
    }

    public PaypalPaymentException(String message) {
        super(message);
    }
}
