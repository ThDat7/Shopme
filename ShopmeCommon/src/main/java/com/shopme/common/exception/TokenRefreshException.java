package com.shopme.common.exception;

public class TokenRefreshException extends RuntimeException{

    public TokenRefreshException(String token, String message) {
        super(String.format("Fail for [%s]: [%s]", token, message));
    }
}
