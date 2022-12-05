package com.shopme.security;

import com.shopme.common.entity.CustomerUserRefreshToken;
import com.shopme.common.security.RefreshTokenDetails;
import com.shopme.common.security.RefreshTokenService;
import com.shopme.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerUserRefreshTokenService extends RefreshTokenService {
    private CustomerRepository customerRepository;

    private CustomerUserRefreshTokenRepository refreshTokenRepository;

    public CustomerUserRefreshTokenService(CustomerRepository customerRepository, CustomerUserRefreshTokenRepository refreshTokenRepository) {
        this.customerRepository = customerRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    protected RefreshTokenDetails findByUserId(int userId) {
        Optional<CustomerUserRefreshToken> customerUserRefreshToken = refreshTokenRepository
                .findByUserId(userId);
        if (!customerUserRefreshToken.isPresent()) return null;

        RefreshTokenDetails refreshToken = new CustomerUserRefreshTokenDetails(
                customerUserRefreshToken.get());

        return refreshToken;
    }

    @Override
    protected RefreshTokenDetails save(RefreshTokenDetails refreshTokenDetails) {
        CustomerUserRefreshToken customerUserRefreshToken =
                (CustomerUserRefreshToken) refreshTokenDetails.getRefreshToken();

        refreshTokenRepository.save(customerUserRefreshToken);

        return new CustomerUserRefreshTokenDetails(customerUserRefreshToken);
    }

    @Override
    public Optional<RefreshTokenDetails> findByToken(String token) {
        Optional<CustomerUserRefreshToken> customerUserRefreshToken = refreshTokenRepository
                .findByToken(token);
        if (!customerUserRefreshToken.isPresent()) return Optional.empty();
        RefreshTokenDetails refreshToken = new CustomerUserRefreshTokenDetails(
                customerUserRefreshToken.get());

        return Optional.of(refreshToken);
    }

    @Override
    protected void deleteByUserId(int userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    @Override
    protected RefreshTokenDetails newRefreshTokenDetails() {

        return new CustomerUserRefreshTokenDetails(
                new CustomerUserRefreshToken());
    }

    @Override
    public void deleteByUsername(String username) {
        int userId = customerRepository.getIdByUsername(username);
        refreshTokenRepository.deleteByUserId(userId);
    }

    @Override
    public String getUsername(RefreshTokenDetails refreshTokenDetails) {
        int userId = refreshTokenDetails.getUserId();
        String email = customerRepository.findById(userId).get().getEmail();

        return email;
    }

    @Override
    public int getUserIdByUsername(String username) {
        return customerRepository.getIdByUsername(username);
    }
}
