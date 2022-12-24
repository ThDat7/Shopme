package com.shopme.admin.repository;

import com.shopme.common.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    @Query("SELECT NEW com.shopme.common.entity.OrderDetail(d.product.category.name, " +
            "d.quantity, d.productCost, d.shippingCost, d.subtotal) " +
            "FROM OrderDetail d WHERE d.order.orderTime BETWEEN :startTime AND :endTime")
    List<OrderDetail> findWithCategoryAndTimeBetween(Date startTime, Date endTime);

    @Query("SELECT NEW com.shopme.common.entity.OrderDetail(d.quantity, " +
            "d.product.name,d.productCost, d.shippingCost, d.subtotal) " +
            "FROM OrderDetail d WHERE d.order.orderTime BETWEEN :startTime AND :endTime")
    List<OrderDetail> findWithProductAndTimeBetween(Date startTime, Date endTime);
}
