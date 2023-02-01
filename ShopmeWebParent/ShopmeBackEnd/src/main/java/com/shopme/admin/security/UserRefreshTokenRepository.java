package com.shopme.admin.security;

import com.shopme.common.entity.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRefreshTokenRepository extends
        JpaRepository<UserRefreshToken, Integer> {

    Optional<UserRefreshToken> findByUserId(int userId);

    Optional<UserRefreshToken> findByToken(String token);

    void deleteByUserId(int userId);
}
