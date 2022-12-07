package com.shopme.controller;

import com.shopme.security.JwtService;
import com.shopme.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private CartItemService cartItemService;

    @PostMapping("/add/{productId}/{{quantity}")
    public ResponseEntity<?> addProductToCart(@PathVariable("productId") int productId,
                                           @PathVariable("quantity") int quantity,
                                           HttpServletRequest request) {
        String token = request.getHeader(JwtService.HEADER);
        int customerId = jwtService.getCustomerId(token);

        int updatedQuantity = cartItemService
                .addProduct(productId, quantity, customerId);

        return ResponseEntity.ok(updatedQuantity);
    }
}
