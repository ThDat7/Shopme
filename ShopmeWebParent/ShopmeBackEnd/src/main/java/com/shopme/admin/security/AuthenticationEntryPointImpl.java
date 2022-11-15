package com.shopme.admin.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.admin.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ErrorResponse errorResponse =
                new ErrorResponse(HttpStatus.UNAUTHORIZED, authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(errorResponse.getErrorCode().value());

        OutputStream out = response.getOutputStream();
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, errorResponse);
        out.flush();
    }

}
