package com.shopme.admin.security.service;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class JWTUtils {
    static final int EXPIRATION_TIME = 3 * 60 * 1000;
    static final String SECRET = "ThanhDatSecret";
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String HEADER = "Authorization";

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTUtils.class);

    public static String generateJWTToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET)
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

    public static String getSubject(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public static String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(HEADER);
        if (StringUtils.hasText(headerAuth) &&
                headerAuth.startsWith(TOKEN_PREFIX))
            return headerAuth.replace(TOKEN_PREFIX, "").trim();

        return null;
    }
}
