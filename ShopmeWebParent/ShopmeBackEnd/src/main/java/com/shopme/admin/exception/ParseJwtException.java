package com.shopme.admin.exception;

import org.springframework.security.core.AuthenticationException;

public class ParseJwtException extends AuthenticationException {
    public ParseJwtException(String msg) {
        super(msg);
    }
}
