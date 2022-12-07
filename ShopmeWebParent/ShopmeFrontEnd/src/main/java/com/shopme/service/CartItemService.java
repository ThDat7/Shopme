package com.shopme.service;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Product;
import com.shopme.common.exception.ResourceNotFoundException;
import com.shopme.exception.CartItemOutOfQuantityException;
import com.shopme.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartItemService {
    @Value("${shopme.app.product.max-quantity-product-can-buy}")
    private int maxQuantity;

    @Autowired
    private CartItemRepository cartItemRepository;

    public int addProduct(int productId, int quantity, int customerId) {
        Customer customer = new Customer(customerId);
        Product product = new Product(productId);
        int totalQuantity;

        Optional<CartItem> opCartItem = cartItemRepository
                .findByCustomerAndProduct(customer, product);
        CartItem cartItem;
        if (opCartItem.isPresent()) {
            cartItem = opCartItem.get();
            totalQuantity = cartItem.getQuantity() + quantity;
            if (maxQuantity >= 0 && totalQuantity > maxQuantity)
                throw new CartItemOutOfQuantityException();
        } else {
            cartItem = new CartItem(productId, customerId);
            totalQuantity = quantity;
        }

        cartItem.setQuantity(totalQuantity);
        cartItemRepository.save(cartItem);

        return totalQuantity;

    }

}
