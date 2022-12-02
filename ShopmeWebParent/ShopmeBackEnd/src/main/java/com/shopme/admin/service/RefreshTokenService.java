package com.shopme.admin.service;

import com.shopme.common.exception.TokenRefreshException;
import com.shopme.admin.repository.UserRepository;
import com.shopme.common.entity.RefreshToken;
import com.shopme.admin.repository.RefreshTokenRepository;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${shopme.app.jwt.refresh.ExpirationMs}")
    private Long refreshTokenDurationMs;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public RefreshToken createRefreshToken(int userId) {
        Instant expiryDate = Instant.now().plusMillis(refreshTokenDurationMs);
        String token = UUID.randomUUID().toString();

        Optional<RefreshToken> hasOldToken = refreshTokenRepository.findByUserId(userId);
        if (hasOldToken.isPresent()) {
            RefreshToken oldToken = hasOldToken.get();
            oldToken.setToken(token);
            oldToken.setExpiryDate(expiryDate);
            refreshTokenRepository.save(oldToken);
            return oldToken;
        }

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUserId(userId);
        refreshToken.setExpiryDate(expiryDate);
        refreshToken.setToken(token);

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public User getUser(RefreshToken refreshToken) {
        return userRepository.findById(refreshToken.getUserId()).get();
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired.");
        }

        return token;
    }

    @Transactional
    public void deleteByUserId(int userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
