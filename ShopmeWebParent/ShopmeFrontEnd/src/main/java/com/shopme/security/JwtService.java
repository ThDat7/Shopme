package com.shopme.security;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Customer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

@Service
public class JwtService implements com.shopme.common.security.service.JwtService {
    @Value("${shopme.app.jwt.Secret}")
    private String Secret;

    private static class CustomerJwtClaims {
        public static final String USERNAME = "Username";
        public static final String AUTHENTICATION_TYPE = "Authentication_Type";
    }

    @Value("${shopme.app.jwt.ExpirationMs}")
    private int ExpirationMs;
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String HEADER = "Authorization";

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);

    @Override
    public String generateToken(Object principal) {
        Customer customer = (Customer) principal;

        HashMap<String, Object> claims = new HashMap<>();
        claims.put(CustomerJwtClaims.AUTHENTICATION_TYPE,
                customer.getAuthenticationType().toString()
        );

        claims.put(CustomerJwtClaims.USERNAME,
                customer.getEmail());



        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + ExpirationMs))
                .signWith(SignatureAlgorithm.HS512, Secret)
                .compact();
    }

    private Claims getClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(Secret)
                .parseClaimsJws(token)
                .getBody();

    }

    @Override
    public String getUsername(String token) {
        return (String) getClaims(token)
                .get(CustomerJwtClaims.USERNAME);
    }


    public AuthenticationType getAuthenticationType(String token) {
        return (AuthenticationType) getClaims(token)
                .get(CustomerJwtClaims.AUTHENTICATION_TYPE);
    }
}
