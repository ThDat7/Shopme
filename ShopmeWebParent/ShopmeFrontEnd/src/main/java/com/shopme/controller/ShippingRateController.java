package com.shopme.controller;

import com.shopme.security.JwtService;
import com.shopme.service.ShippingRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/shipping-rates")
public class ShippingRateController {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private ShippingRateService shippingRateService;

    @PostMapping("/get")
    public ResponseEntity<?> getForCustomer(HttpServletRequest request) {
        int customerId = getCustomerId(request);

        return ResponseEntity.ok(
                shippingRateService.getForCustomer(customerId));
    }

    private int getCustomerId(HttpServletRequest request) {
        String token = request.getHeader(JwtService.HEADER);
        return jwtService.getCustomerId(token);
    }
}
