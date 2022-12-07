package com.shopme.common.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.common.security.CustomUserDetails;
import com.shopme.common.security.RefreshTokenDetails;
import com.shopme.common.security.RefreshTokenService;
import com.shopme.common.security.payload.response.JwtResponse;
import com.shopme.common.security.service.JwtService;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {
    private RefreshTokenService refreshTokenService;

    private JwtService jwtService;


    public JWTLoginFilter(JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        return getAuthenticationManager().authenticate(new
                UsernamePasswordAuthenticationToken(email, password ));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
        Object principal = userDetails.getPrincipal();
        String username = userDetails.getUsername();

        int userId = refreshTokenService.getUserIdByUsername(username);

        String accessToken = jwtService.generateToken(principal);
        RefreshTokenDetails userRefreshTokenDetails = refreshTokenService
                .createRefreshToken(userId);
        JwtResponse jwtResponse = new JwtResponse(accessToken, userRefreshTokenDetails.getToken());

        ((UsernamePasswordAuthenticationToken) authResult)
                .setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), jwtResponse);
    }
}
