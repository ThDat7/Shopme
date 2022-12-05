package com.shopme.admin.security;

import com.shopme.common.entity.UserRefreshToken;
import com.shopme.common.security.RefreshTokenDetails;

import java.time.Instant;

public class UserRefreshTokenDetails implements RefreshTokenDetails {
    private UserRefreshToken userRefreshToken;

    @Override
    public int getUserId() {
        return userRefreshToken.getUserId();
    }

    @Override
    public String getToken() {
        return userRefreshToken.getToken();
    }

    @Override
    public Instant getExpiryDate() {
        return userRefreshToken.getExpiryDate();
    }

    @Override
    public void setUserId(int userId) {
        userRefreshToken.setUserId(userId);
    }

    @Override
    public void setToken(String token) {
        userRefreshToken.setToken(token);
    }

    @Override
    public void setExpiryDate(Instant expiryDate) {
        userRefreshToken.setExpiryDate(expiryDate);
    }

    public UserRefreshToken getRefreshToken() {
        return userRefreshToken;
    }

    public UserRefreshTokenDetails(UserRefreshToken userRefreshToken) {
        this.userRefreshToken = userRefreshToken;
    }
}
