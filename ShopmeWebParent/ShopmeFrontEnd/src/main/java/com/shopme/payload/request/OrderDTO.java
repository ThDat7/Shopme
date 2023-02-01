package com.shopme.payload.request;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.OrderDetail;
import com.shopme.common.entity.OrderStatus;
import com.shopme.common.entity.PaymentMethod;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class OrderDTO {
    private String country;

    private Date orderTime;

    private float shippingCost;
    private float productCost;
    private float subtotal;
    private float tax;
    private float total;

    private int deliverDays;
    private Date deliverDate;

    private PaymentMethod paymentMethod;
    private OrderStatus status;

    private Customer customer;

    private Set<OrderDetail> orderDetails = new HashSet<>();
}
