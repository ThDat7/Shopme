package com.shopme.service;

import com.shopme.common.entity.Product;
import com.shopme.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ProductService {
    public static final int PRODUCT_PER_PAGE = 10;

    @Autowired
    private ProductRepository productRepository;


    public Page<Product> listByCategory(int pageIndex, Integer categoryId) {
        Pageable pageable = PageRequest.of(pageIndex - 1, PRODUCT_PER_PAGE);
        String categoryIdMatch = "-" + categoryId + "-";

        return productRepository.listByCategory(categoryId, categoryIdMatch, pageable);
    }

    public Page<Product> getAll(HashMap<String, String> requestParams) {
        return null;
    }
}
