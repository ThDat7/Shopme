package com.shopme.common.security.service;


public interface JwtService {

    public static final String TOKEN_PREFIX = "Bearer";
    public static final String HEADER = "Authorization";

    public String generateToken(Object principal);

    public String getUsername(String token);
}
