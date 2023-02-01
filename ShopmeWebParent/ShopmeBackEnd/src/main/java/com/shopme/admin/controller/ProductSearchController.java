package com.shopme.admin.controller;

import com.shopme.admin.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController("/orders/search_product")
public class ProductSearchController {
    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<?> searchProducts(
            HashMap<String, String> requestParams) {
        return ResponseEntity.ok(productService.getAll(requestParams));
    }
}
