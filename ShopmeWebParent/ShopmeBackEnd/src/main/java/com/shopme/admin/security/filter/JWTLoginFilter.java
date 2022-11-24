package com.shopme.admin.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.admin.payload.response.JwtResponse;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.admin.security.service.JwtService;
import com.shopme.admin.service.RefreshTokenService;
import com.shopme.common.entity.RefreshToken;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
        ShopmeUserDetails user = (ShopmeUserDetails) authResult.getPrincipal();

        String username = user.getUsername();
        int userId = user.getId();

        String accessToken = jwtService.generateToken(username);
        RefreshToken refreshToken = refreshTokenService
                .createRefreshToken(userId);
        JwtResponse jwtResponse = new JwtResponse(accessToken, refreshToken.getToken());

        ((UsernamePasswordAuthenticationToken) authResult)
                .setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), jwtResponse);
    }
}
