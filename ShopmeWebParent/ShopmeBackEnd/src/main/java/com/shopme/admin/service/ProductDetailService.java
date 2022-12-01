package com.shopme.admin.service;

import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductDetail;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Service
public class ProductDetailService {
    public void setProductDetails(HashSet<ProductDetail> details,
                                  Product product) {
        if (details.isEmpty() || details == null) return;

        product.setDetails(new ArrayList<>(details));
    }
}
