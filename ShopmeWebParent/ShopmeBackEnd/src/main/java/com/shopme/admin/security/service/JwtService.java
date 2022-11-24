package com.shopme.admin.security.service;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class JwtService {
    @Value("${shopme.app.jwt.Secret}")
    private String Secret;

    @Value("${shopme.app.jwt.ExpirationMs}")
    private int ExpirationMs;
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String HEADER = "Authorization";

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + ExpirationMs))
                .signWith(SignatureAlgorithm.HS512, Secret)
                .compact();
    }

    public String getUsername(String token) {
        return Jwts
                .parser()
                .setSigningKey(Secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(Secret)
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            LOGGER.error("JWT expired", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            LOGGER.error("Token is null, empty or only whitespace", ex.getMessage());
        } catch (MalformedJwtException ex) {
            LOGGER.error("JWT is invalid", ex);
        } catch (UnsupportedJwtException ex) {
            LOGGER.error("JWT is not supported", ex);
        } catch (SignatureException ex) {
            LOGGER.error("Signature validation failed");
        }
        return false;
    }
}
