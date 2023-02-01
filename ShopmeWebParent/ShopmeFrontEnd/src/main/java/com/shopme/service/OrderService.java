package com.shopme.service;

import com.shopme.common.entity.*;
import com.shopme.common.exception.OrderStatusExistException;
import com.shopme.common.exception.ResourceNotFoundException;
import com.shopme.common.paramFilter.RequestParamsHelper;
import com.shopme.paramFilter.OrderParamFilter;
import com.shopme.payload.request.OrderReturnRequest;
import com.shopme.payload.response.CheckoutInfo;
import com.shopme.repository.CustomerRepository;
import com.shopme.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AddressService addressService;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private ShippingRateService shippingRateService;

    @Autowired
    private CustomerRepository customerRepository;


    public Order createOrder(int customerId, PaymentMethod paymentMethod) {
        Customer customer = new Customer(customerId);
        Order order = new Order();

        List<CartItem> cartItems = shoppingCartService.getAll(customerId);
        ShippingRate shippingRate = shippingRateService
                .getForCustomer(customerId);

        CheckoutInfo checkoutInfo = checkoutService
                .prepareCheckout(cartItems, shippingRate);

        order.setOrderTime(new Date());

        if (paymentMethod.equals(PaymentMethod.COD))
            order.setStatus(OrderStatus.NEW);
        else if (paymentMethod.equals(PaymentMethod.PAYPAL))
            order.setStatus(OrderStatus.PAID);

        order.setCustomer(customer);
        order.setProductCost(checkoutInfo.getProductCost());
        order.setSubtotal(checkoutInfo.getProductTotal());
        order.setShippingCost(checkoutInfo.getShippingCostTotal());
        order.setTax(0.0f);
        order.setTotal(checkoutInfo.getPaymentTotal());
        order.setDeliverDate(checkoutInfo.getDeliverDate());
        order.setDeliverDays(checkoutInfo.getDeliverDays());

        try
        {
            Address defaultAddress = addressService.getDefault(customerId);
            modelMapper.map(defaultAddress, order);
        } catch(ResourceNotFoundException e) {
            customer = customerRepository.findById(customerId).get();
            modelMapper.map(customer, order);
        }
        order.setId(null);


        order.setOrderDetails(
                prepareOrderDetails(customerId, order, shippingRate));

        OrderTrack track = new OrderTrack();
        track.setOrder(order);
        track.setStatus(OrderStatus.NEW);
        track.setNote(OrderStatus.NEW.defaultDescription());
        track.setUpdateTime(new Date());

        order.getOrderTracks().add(track);

        return orderRepository.save(order);
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

    public Page<Order> getAll(Integer customerId, HashMap<String, String> requestParams) {
        Pageable pageable = RequestParamsHelper
                .getPageableFromParamRequest(requestParams);

        for (String key : requestParams.keySet()) {
            OrderParamFilter orderParamFilter;
            try {
                orderParamFilter = OrderParamFilter.valueOf(key);
            } catch (IllegalArgumentException e) { continue; }

            switch (orderParamFilter) {
                case keyword: {
                    String keyword = requestParams.get(key);
                    OrderStatus orderStatus = null;
                    try {
                        orderStatus = OrderStatus.valueOf(keyword);
                    } catch (IllegalArgumentException e) {}

                    return orderRepository.findAll(customerId, keyword, orderStatus, pageable);
                }
            }
        }
        return orderRepository.findAll(customerId, pageable);
    }

    public Order get(int id, int customerId) {
        return orderRepository.findByIdAndCustomer(id, new Customer(customerId))
                .orElseThrow(ResourceNotFoundException::new);
    }

    public void setOrderReturnRequest(OrderReturnRequest orderReturnRequest, int customerId) {
        Order order = orderRepository.findById(orderReturnRequest.getOrderId())
                .orElseThrow(ResourceNotFoundException::new);

        order.getOrderTracks().stream()
                .forEach(orderTrack -> {
                    if (orderTrack.getStatus().equals(OrderStatus.RETURN_REQUESTED))
                        throw new OrderStatusExistException();
                });

        OrderTrack track = new OrderTrack();
        track.setStatus(OrderStatus.RETURN_REQUESTED);
        track.setOrder(order);
        track.setUpdateTime(new Date());

        String note = "Reason " + orderReturnRequest.getReason();
        if (!orderReturnRequest.getNote().isEmpty())
            note += ". " + orderReturnRequest.getNote();
        track.setNote(note);


        order.getOrderTracks().add(track);
        order.setStatus(OrderStatus.RETURN_REQUESTED);

        orderRepository.save(order);
    }
}
