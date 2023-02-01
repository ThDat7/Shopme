package com.shopme.controller;

import com.shopme.payload.request.OrderReturnRequest;
import com.shopme.payload.response.OrderReturnResponse;
import com.shopme.security.JwtService;
import com.shopme.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController("/orders")
public class OrderController {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<?> getAll(HashMap<String, String> requestParam,
                                    HttpServletRequest request) {
        int customerId = getCustomerId(request);
        return ResponseEntity.ok(
                orderService.getAll(customerId, requestParam));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") int id,
                                 HttpServletRequest request) {
        int customerId = getCustomerId(request);
        return ResponseEntity.ok(orderService.get(id, customerId));
    }

    @PostMapping("/orders/return")
    public ResponseEntity<?> handleOrderReturnedRequest(
            @RequestBody OrderReturnRequest returnRequest,
            HttpServletRequest request) {
        int customerId = getCustomerId(request);
        orderService.setOrderReturnRequest(returnRequest, customerId);

        return ResponseEntity.ok(
                new OrderReturnResponse(returnRequest.getOrderId())
        );
    }

    private Integer getCustomerId(HttpServletRequest request) {
        String jwt = request.getParameter(JwtService.HEADER);
        return jwtService.getCustomerId(jwt);
    }
}
