package com.shopme.exception;

public class OutOfMaxAddressBook extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Out of max address book";

    public OutOfMaxAddressBook() {
        this(DEFAULT_MESSAGE);
    }

    public OutOfMaxAddressBook(String message) {
        super(message);
    }
}
