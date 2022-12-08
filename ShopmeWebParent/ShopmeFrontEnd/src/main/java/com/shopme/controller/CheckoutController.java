package com.shopme.controller;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.payload.response.CheckoutInfo;
import com.shopme.security.JwtService;
import com.shopme.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ShippingRateService shippingRateService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping
    public ResponseEntity<?> getCheckout(HttpServletRequest request) {
        int customerId = getCustomerId(request);

        ShippingRate shippingRate = shippingRateService
                .getForCustomer(customerId);
        List<CartItem> cartItems = shoppingCartService.getAll(customerId);

        CheckoutInfo checkoutInfo = checkoutService
                .prepareCheckout(cartItems, shippingRate);

        return ResponseEntity.ok(checkoutInfo);
    }

    private int getCustomerId(HttpServletRequest request) {
        String token = request.getHeader(JwtService.HEADER);
        return jwtService.getCustomerId(token);
    }
}
