package com.shopme.admin.security.filter;

import com.shopme.admin.security.service.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private UserDetailsService userDetailsService;

    private JwtService jwtService;

    public JwtAuthorizationFilter(UserDetailsService userDetailsService, JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String tokenHeader = request.getHeader(JwtService.HEADER);

        if (tokenHeader == null || !tokenHeader.startsWith(JwtService.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
        String token = tokenHeader.substring(JwtService.TOKEN_PREFIX.length());
        String username = jwtService.getUsername(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch(Exception e) {

        }
        filterChain.doFilter(request, response);
    }
}
