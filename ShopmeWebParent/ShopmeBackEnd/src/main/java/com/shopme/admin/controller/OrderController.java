package com.shopme.admin.controller;

import com.shopme.admin.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(orderService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") int id) {
        return ResponseEntity.ok(orderService.get(id));
    }

    @GetMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") int id) {
        orderService.delete(id);
    }

    @PostMapping("/shipper/update/{id}/{status}")
    public void updateOrderStatus(@PathVariable("id") int orderId,
                                  @PathVariable("status") String status) {
        orderService.updateStatus(orderId, status);
    }

}
