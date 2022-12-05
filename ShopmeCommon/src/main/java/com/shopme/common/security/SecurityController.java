package com.shopme.common.security;

import com.shopme.common.exception.ResourceAlreadyExistException;
import com.shopme.common.exception.TokenRefreshException;
import com.shopme.common.security.payload.request.TokenRefreshRequest;
import com.shopme.common.security.payload.response.TokenRefreshResponse;
import com.shopme.common.security.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class SecurityController {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RefreshTokenService refreshTokenService;



    @GetMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest requestRefreshToken) {
        String refreshToken = requestRefreshToken.getRefreshToken();

        return refreshTokenService
                .findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(refreshTokenService::getUsername)
                .map(username -> {
                    String accessToken = jwtService.generateToken(username);
                    return ResponseEntity.ok(new TokenRefreshResponse(accessToken, refreshToken));
                }).orElseThrow(() -> new TokenRefreshException(refreshToken, "Invalid refresh token."));

    }

    @GetMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logoutUser() {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String username = user.getUsername();
        refreshTokenService.deleteByUsername(username);
    }

}
