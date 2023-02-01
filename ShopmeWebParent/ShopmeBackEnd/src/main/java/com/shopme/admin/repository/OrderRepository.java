package com.shopme.admin.repository;

import com.shopme.common.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT NEW com.shopme.common.entity.Order(o.id, " +
            "o.orderTime, o.productCost, o.subtotal, o.total) " +
            "FROM Order o WHERE o.orderTime BETWEEN :startTime AND :endTime")
    List<Order> findByOrderTimeBetween(Date startTime, Date endTime);

}
