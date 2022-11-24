package com.shopme.admin.controller;

import com.shopme.admin.exception.TokenRefreshException;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.admin.payload.request.TokenRefreshRequest;
import com.shopme.admin.payload.response.TokenRefreshResponse;
import com.shopme.admin.security.service.JwtService;
import com.shopme.admin.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
                .map(refreshTokenService::getUser)
                .map(user -> {
                    String accessToken = jwtService.generateToken(user.getEmail());
                   return ResponseEntity.ok(new TokenRefreshResponse(accessToken, refreshToken));
                }).orElseThrow(() -> new TokenRefreshException(refreshToken, "Invalid refresh token."));

    }

    @GetMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logoutUser() {
        ShopmeUserDetails user = (ShopmeUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        int userId = user.getId();
        refreshTokenService.deleteByUserId(userId);
    }

}
