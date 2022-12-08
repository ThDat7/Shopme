package com.shopme.controller;

import com.shopme.common.entity.*;
import com.shopme.payload.request.OrderDTO;
import com.shopme.payload.response.CheckoutInfo;
import com.shopme.security.JwtService;
import com.shopme.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private ShippingRateService shippingRateService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<?> getCheckout(HttpServletRequest request) {
        int customerId = getCustomerId(request);

        CheckoutInfo checkoutInfo = checkoutService
                .prepareCheckout(customerId);

        return ResponseEntity.ok(checkoutInfo);
    }

    private int getCustomerId(HttpServletRequest request) {
        String token = request.getHeader(JwtService.HEADER);
        return jwtService.getCustomerId(token);
    }

    @PostMapping("/place-order")
    @ResponseStatus(HttpStatus.OK)
    public void placeOrder(OrderDTO orderDTO
            , HttpServletRequest request) {
        int customerId = getCustomerId(request);

        Order order = covertToEntity(orderDTO);
        orderService.createOrder(customerId, order);
        shoppingCartService.deleteAllByCustomer(customerId);
    }

    private Order covertToEntity(OrderDTO orderDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(orderDTO, Order.class);
    }
}
