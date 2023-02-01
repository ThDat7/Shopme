package com.shopme.common.security;

import com.shopme.common.exception.TokenRefreshException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public abstract class RefreshTokenService {

    @Value("${shopme.app.jwt.refresh.ExpirationMs}")
    private Long refreshTokenDurationMs;

//    @Qualifier("userRefreshTokenRepository")
//    private AbstractUserRepository userRepository;
//    private RefreshTokenRepository refreshTokenRepository;


    protected abstract RefreshTokenDetails findByUserId(int userId);

    protected abstract RefreshTokenDetails save(RefreshTokenDetails refreshTokenDetails);

    public abstract Optional<RefreshTokenDetails> findByToken(String token);

    protected abstract void deleteByUserId(int userId);

    protected abstract RefreshTokenDetails newRefreshTokenDetails();

    @Transactional
    public abstract void deleteByUsername(String username);

    public abstract String getUsername(RefreshTokenDetails refreshTokenDetails);

    public abstract Object getPrincipal(RefreshTokenDetails refreshTokenDetails);

    public abstract int getUserIdByUsername(String username);

    public RefreshTokenDetails createRefreshToken(int userId) {
        Instant expiryDate = Instant.now().plusMillis(refreshTokenDurationMs);
        String token = UUID.randomUUID().toString();

        RefreshTokenDetails oldToken = findByUserId(userId);
        if (oldToken != null) {
            oldToken.setToken(token);
            oldToken.setExpiryDate(expiryDate);
            save(oldToken);
            return oldToken;
        }

        RefreshTokenDetails refreshTokenDetails = newRefreshTokenDetails();

        refreshTokenDetails.setUserId(userId);
        refreshTokenDetails.setExpiryDate(expiryDate);
        refreshTokenDetails.setToken(token);

        refreshTokenDetails = save(refreshTokenDetails);
        return refreshTokenDetails;
    }

    public RefreshTokenDetails verifyExpiration(RefreshTokenDetails token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            deleteByUserId(token.getUserId());
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired.");
        }

        return token;
    }


}
