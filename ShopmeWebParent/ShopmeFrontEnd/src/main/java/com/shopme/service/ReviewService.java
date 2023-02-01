package com.shopme.service;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Order;
import com.shopme.common.entity.OrderDetail;
import com.shopme.common.entity.Review;
import com.shopme.common.exception.ResourceNotFoundException;
import com.shopme.repository.OrderRepository;
import com.shopme.repository.ProductRepository;
import com.shopme.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class ReviewService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    public Review get(int customerId, int orderDetailId) {
        return reviewRepository
                .findByCustomerAndOrderDetail(new Customer(customerId)
                        , new OrderDetail(orderDetailId))
                .orElseThrow(ResourceNotFoundException::new);
    }

    public void save(int customerId, Review review) {
        Customer customer = new Customer(customerId);
        int orderDetailId = review.getOrderDetail().getId();
        boolean check = false;

        List<Order> orderList = orderRepository.findAllDelivered(customerId);

        for (Order order : orderList) {
            Set<OrderDetail> orderDetails = order.getOrderDetails();
            for (OrderDetail orderDetail : orderDetails)
                if (orderDetail.getId() == orderDetailId) {
                    check = true;
                    review.setOrderDetail(orderDetail);
                }
        }

        if (!check)
            throw new ResourceNotFoundException();

        review.setCustomer(customer);
        review.setReviewTime(new Date());
        reviewRepository.save(review);
        productRepository.updateAvgRatingAndReviewCount(
                review.getOrderDetail().getProduct().getId());
    }

    public void like(int customerId, int reviewId) {
        reviewRepository.updateLike(customerId, reviewId);
    }

    public List<Review> getTop3Reviews(int productId) {
        Sort sort = Sort.by("countLike").descending();
        Pageable pageable = PageRequest.of(0, 3, sort);

        return reviewRepository.findByProduct(productId, pageable);
    }
}
