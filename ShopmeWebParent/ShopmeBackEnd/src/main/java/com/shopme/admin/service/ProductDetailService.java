package com.shopme.admin.service;

import com.shopme.common.entity.Product;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ProductDetailService {
    public void setProductDetails(HashMap<String, String> details,
                                   Product product) {
        if (details.isEmpty() || details == null) return;

        for (String key : details.keySet()) {
            String value = details.get(key);

            if (!key.isEmpty() && !value.isEmpty())
                product.addDetail(key, value);
        }
    }
}
