package com.shopme.service;

import com.shopme.common.entity.*;
import com.shopme.payload.response.CheckoutInfo;
import com.shopme.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private ShippingRateService shippingRateService;


    public void createOrder(int customerId, Order order) {
        Customer customer = new Customer(customerId);

        List<CartItem> cartItems = shoppingCartService.getAll(customerId);
        ShippingRate shippingRate = shippingRateService
                .getForCustomer(customerId);

        CheckoutInfo checkoutInfo = checkoutService
                .prepareCheckout(cartItems, shippingRate);

        order.setOrderTime(new Date());
        order.setStatus(OrderStatus.NEW);
        order.setCustomer(customer);
        order.setProductCost(checkoutInfo.getProductCost());
        order.setSubtotal(checkoutInfo.getProductTotal());
        order.setShippingCost(checkoutInfo.getShippingCostTotal());
        order.setTax(0.0f);
        order.setTotal(checkoutInfo.getPaymentTotal());
        order.setDeliverDate(checkoutInfo.getDeliverDate());
        order.setDeliverDays(checkoutInfo.getDeliverDays());

        order.setOrderDetails(
                prepareOrderDetails(customerId, order, shippingRate));

        orderRepository.save(order);
    }

    private Set<OrderDetail> prepareOrderDetails(int customerId
            , Order newOrder, ShippingRate shippingRate) {
        List<CartItem> cartItems = shoppingCartService.getAll(customerId);

        Set<OrderDetail> orderDetails = newOrder.getOrderDetails();
        for (CartItem item : cartItems) {
            Product product = item.getProduct();

            OrderDetail orderDetail = new OrderDetail();

            orderDetail.setOrder(newOrder);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(item.getQuantity());
            orderDetail.setUnitPrice(product.getPrice()
                    * (1.0f - product.getDiscountPercent()));
            orderDetail.setProductCost(product.getCost() * item.getQuantity());
            orderDetail.setSubtotal(product.getPrice() * item.getQuantity());
            orderDetail.setShippingCost(
                    checkoutService.getShippingCost(item, shippingRate)
            );

            orderDetails.add(orderDetail);
        }

        return orderDetails;
    }
}
