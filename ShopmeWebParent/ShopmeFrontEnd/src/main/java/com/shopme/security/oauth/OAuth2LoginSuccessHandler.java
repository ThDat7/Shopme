package com.shopme.security.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Customer;
import com.shopme.common.security.RefreshTokenDetails;
import com.shopme.common.security.RefreshTokenService;
import com.shopme.common.security.payload.response.JwtResponse;
import com.shopme.common.security.service.JwtService;
import com.shopme.security.CustomerUserDetails;
import com.shopme.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2LoginSuccessHandler.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private CustomerService customerService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        CustomerOAuth2User oAuth2User =(CustomerOAuth2User)
                authentication.getPrincipal();

        String name = oAuth2User.getName();
        String email = oAuth2User.getEmail();
        String countryCode = request.getLocale().getCountry();
        String clientName = oAuth2User.getClientName();

         AuthenticationType authenticationType = getAuthenticationType(clientName);

        Optional<Customer> opCustomer = customerService.getByEmail(email);
        Customer customer = opCustomer.get();

        if (customer != null)
            customerService.updateAuthenticationType(customer, authenticationType);
        else {
            customer = customerService
                    .addNewCustomerUponOAuthLogin(name, email, countryCode, authenticationType);
        }

        int userId = customer.getId();

        String accessToken = jwtService.generateToken(email);
        RefreshTokenDetails userRefreshTokenDetails = refreshTokenService
                .createRefreshToken(userId);
        JwtResponse jwtResponse = new JwtResponse(accessToken, userRefreshTokenDetails.getToken());

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), jwtResponse);

        super.onAuthenticationSuccess(request, response, authentication);
    }


    private AuthenticationType getAuthenticationType(String clientName) {
        try {
            return AuthenticationType.valueOf(clientName.toUpperCase());
        }
        catch (IllegalArgumentException e) {
            LOGGER.error("Don't support " + clientName + " authentication type.");
            throw e;
        }
    }

}
