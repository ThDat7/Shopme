package com.shopme.repository;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    List<CartItem> findByCustomer(Customer customer);

    Optional<CartItem> findByCustomerAndProduct(Customer customer, Product product);

    @Modifying
    @Query("UPDATE CartItem c SET c.quantity = :quantity WHERE " +
            "c.customer.id = :customerId AND c.product.id = :productId")
    void updateQuantity(int quantity, int customerId, int productId);

    @Modifying
    @Query("DELETE FROM CartItem  c WHERE c.customer.id = :customerId " +
            "AND c.product.id = :productId")
    void deleteByCustomerAndProduct(int customerId, int productId);
}
