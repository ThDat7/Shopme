package com.shopme.security;

import com.shopme.common.entity.CustomerUserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerUserRefreshTokenRepository extends
        JpaRepository<CustomerUserRefreshToken, Integer> {

    Optional<CustomerUserRefreshToken> findByUserId(int userId);

    Optional<CustomerUserRefreshToken> findByToken(String token);

    void deleteByUserId(int userId);
}
