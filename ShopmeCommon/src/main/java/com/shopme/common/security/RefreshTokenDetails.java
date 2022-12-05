package com.shopme.common.security;

import java.time.Instant;

public interface RefreshTokenDetails {
    int getUserId();

    String getToken();

    Instant getExpiryDate();

    void setUserId(int userId);

    void setToken(String token);

    void setExpiryDate(Instant expiryDate);

    Object getRefreshToken();

}
