package com.shopme.admin.service;

import com.shopme.common.exception.OrderStatusExistException;
import com.shopme.admin.repository.OrderRepository;
import com.shopme.common.entity.Order;
import com.shopme.common.entity.OrderStatus;
import com.shopme.common.entity.OrderTrack;
import com.shopme.common.exception.ResourceNotFoundException;
import com.shopme.common.metamodel.*;
import com.shopme.common.paramFilter.OrderParamFilter;
import com.shopme.common.paramFilter.ProductParamFilter;
import com.shopme.common.paramFilter.RequestParamsHelper;
import com.shopme.common.specification.Filter;
import com.shopme.common.specification.SpecificationHelper;
import com.shopme.common.specification.SpecificationOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Page<Order> getAll(HashMap<String, String> requestParams) {
        Specification specification = Specification.not(null);

        for (String key : requestParams.keySet()) {
            String value = requestParams.get(key);

            OrderParamFilter enumKey;
            try {
                enumKey = OrderParamFilter.valueOf(key);
            } catch(IllegalArgumentException e) { continue; }
            switch (enumKey) {
                case keyword: {
                    String keywordSearch = value;
                    Filter searchLastName = Filter.builder()
                            .field(Order_.LAST_NAME).build();

                    Filter searchFirstName = Filter.builder()
                            .field(Order_.FIRST_NAME).build();

                    Filter searchPhoneNumber = Filter.builder()
                            .field(Order_.PHONE_NUMBER).build();

                    Filter searchAddressLine1 = Filter.builder()
                            .field(Order_.ADDRESS_LINE_1).build();

                    Filter searchAddressLine2 = Filter.builder()
                            .field(Order_.ADDRESS_LINE_2).build();

                    Filter searchPostalCode = Filter.builder()
                            .field(Order_.POSTAL_CODE).build();

                    Filter searchState = Filter.builder()
                            .field(Order_.STATE).build();

                    Filter searchPaymentMethod = Filter.builder()
                            .field(Order_.PAYMENT_METHOD).build();

                    Filter searchStatus = Filter.builder()
                            .field(Order_.STATUS).build();

                    Filter searchCLastNameCustomer = Filter.builder()
                            .joinTables(Arrays.asList(Order_.CUSTOMER))
                            .field(Customer_.LAST_NAME).build();

                    Filter searchFirstNameCustomer = Filter.builder()
                            .joinTables(Arrays.asList(Order_.CUSTOMER))
                            .field(Customer_.FIRST_NAME).build();

                    List<Filter> searchFilters = Arrays.asList(
                            searchLastName, searchFirstName, searchPhoneNumber,
                            searchAddressLine1, searchAddressLine2, searchPostalCode,
                            searchState, searchPaymentMethod, searchStatus,
                            searchCLastNameCustomer, searchFirstNameCustomer
                    );

                    searchFilters.forEach(filter -> {
                        filter.setValue(keywordSearch);
                        filter.setOperator(SpecificationOperator.LIKE);
                    });

                    specification = specification.and(SpecificationHelper
                            .filterSpecification(searchFilters));

                    break;
                }

                case orderDate: {
                    Date orderTime;
                    try {
                        orderTime = new SimpleDateFormat("dd/MM/yyyy")
                                .parse(value);
                    } catch (ParseException e) {continue;}

                    Filter matchOrderTime = Filter.builder()
                            .field(Order_.ORDER_TIME)
                            .value(orderTime)
                            .operator(SpecificationOperator.LIKE).build();

                    Specification matchOrderTimeSpec = SpecificationHelper
                            .createSpecification(matchOrderTime);

                    specification.and(matchOrderTimeSpec);
                    break;
                }
            }
        }

        Pageable pageable = RequestParamsHelper.getPageableFromParamRequest(requestParams);

//        return orderRepository.findAll(specification, pageable);
        return orderRepository.findAll(pageable);
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
