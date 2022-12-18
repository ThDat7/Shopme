package com.shopme.admin.service;

import com.shopme.common.exception.OrderStatusExistException;
import com.shopme.admin.repository.OrderRepository;
import com.shopme.common.entity.Order;
import com.shopme.common.entity.OrderStatus;
import com.shopme.common.entity.OrderTrack;
import com.shopme.common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getAll() {
        return orderRepository.findAll();
    }


    public Order get(int id) {
        return orderRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public void delete(int id) {
        orderRepository.deleteById(id);
    }

    public void updateStatus(Integer orderId, String status) {
        OrderStatus orderStatus = OrderStatus.valueOf(status);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(ResourceNotFoundException::new);

        order.getOrderTracks().stream()
                .forEach(orderTrack -> {
                    if (orderTrack.getStatus().equals(orderStatus))
                        throw new OrderStatusExistException();
                });

        OrderTrack track = new OrderTrack();
        track.setOrder(order);
        track.setStatus(orderStatus);
        track.setNote(orderStatus.defaultDescription());
        track.setUpdateTime(new Date());

        order.getOrderTracks().add(track);

        order.setStatus(orderStatus);
        orderRepository.save(order);
    }
}
