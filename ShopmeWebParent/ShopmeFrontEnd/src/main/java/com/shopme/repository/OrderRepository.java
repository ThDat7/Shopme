package com.shopme.repository;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Order;
import com.shopme.common.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId")
    Page<Order> findAll(Integer customerId, Pageable pageable);

    @Query("SELECT o FROM Order o JOIN o.orderDetails od JOIN od.product p " +
            "WHERE o.customer.id = :customerId AND " +
            "(p.name LIKE %:keyword% OR o.status = :orderStatus)")
    Page<Order> findAll(Integer customerId, String keyword, OrderStatus orderStatus, Pageable pageable);

    Optional<Order> findByIdAndCustomer(Integer id, Customer customer);
}
