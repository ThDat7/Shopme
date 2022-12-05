package com.shopme.admin.security;

import com.shopme.admin.repository.UserRepository;
import com.shopme.common.entity.CustomerUserRefreshToken;
import com.shopme.common.entity.User;
import com.shopme.common.entity.UserRefreshToken;
import com.shopme.common.security.RefreshTokenDetails;
import com.shopme.common.security.RefreshTokenService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRefreshTokenService extends RefreshTokenService {
    private UserRepository userRepository;

    private UserRefreshTokenRepository refreshTokenRepository;

    public UserRefreshTokenService(UserRepository userRepository, UserRefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    protected RefreshTokenDetails findByUserId(int userId) {
        Optional<UserRefreshToken> userRefreshTokenDetails =
                refreshTokenRepository.findByUserId(userId);
        if (!userRefreshTokenDetails.isPresent()) return null;
        RefreshTokenDetails refreshTokenDetails = new UserRefreshTokenDetails(
                userRefreshTokenDetails.get());

        return refreshTokenDetails;
    }

    @Override
    protected RefreshTokenDetails save(RefreshTokenDetails refreshTokenDetails) {
        UserRefreshToken userRefreshToken = (UserRefreshToken)
                refreshTokenDetails.getRefreshToken();

        refreshTokenRepository.save(userRefreshToken);

        return new UserRefreshTokenDetails(userRefreshToken);
    }

    @Override
    public Optional<RefreshTokenDetails> findByToken(String token) {
        Optional<UserRefreshToken> userRefreshToken =
                refreshTokenRepository.findByToken(token);
        if (!userRefreshToken.isPresent()) return Optional.empty();

        RefreshTokenDetails refreshToken = new UserRefreshTokenDetails(
                userRefreshToken.get());
        return Optional.of(refreshToken);

    }

    @Override
    protected void deleteByUserId(int userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    @Override
    protected RefreshTokenDetails newRefreshTokenDetails() {

        return new UserRefreshTokenDetails(new UserRefreshToken());
    }

    @Override
    public void deleteByUsername(String username) {
        int userId = userRepository.getIdByUsername(username);
        refreshTokenRepository.deleteByUserId(userId);
    }

    @Override
    public String getUsername(RefreshTokenDetails refreshTokenDetails) {
        User user = userRepository.findById(refreshTokenDetails.getUserId()).get();

        return user.getEmail();
    }

    @Override
    public int getUserIdByUsername(String username) {
        return userRepository.getIdByUsername(username);
    }
}
