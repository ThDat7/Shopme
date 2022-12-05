package com.shopme.security;

import com.shopme.common.entity.CustomerUserRefreshToken;
import com.shopme.common.security.RefreshTokenDetails;

import java.time.Instant;

public class CustomerUserRefreshTokenDetails implements RefreshTokenDetails {
    private CustomerUserRefreshToken customerUserRefreshToken;

    @Override
    public int getUserId() {
        return customerUserRefreshToken.getUserId();
    }

    @Override
    public String getToken() {
        return customerUserRefreshToken.getToken();
    }

    @Override
    public Instant getExpiryDate() {
        return customerUserRefreshToken.getExpiryDate();
    }

    @Override
    public void setUserId(int userId) {
        customerUserRefreshToken.setUserId(userId);
    }

    @Override
    public void setToken(String token) {
        customerUserRefreshToken.setToken(token);
    }

    @Override
    public void setExpiryDate(Instant expiryDate) {
        customerUserRefreshToken.setExpiryDate(expiryDate);
    }

    @Override
    public CustomerUserRefreshToken getRefreshToken() {
        return customerUserRefreshToken;
    }

    public CustomerUserRefreshTokenDetails(CustomerUserRefreshToken customerUserRefreshToken) {
        this.customerUserRefreshToken = customerUserRefreshToken;
    }
}
