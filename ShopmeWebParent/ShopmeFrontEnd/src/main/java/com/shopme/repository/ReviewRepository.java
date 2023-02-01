package com.shopme.repository;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.OrderDetail;
import com.shopme.common.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Optional<Review> findByCustomerAndOrderDetail(Customer customer, OrderDetail orderDetail);

    @Query("UPDATE Review r SET r.countLike = r.countLike + 1" +
            " WHERE r.customer.id = :customerId AND r.id = :reviewId")
    @Modifying
    void updateLike(int customerId, int reviewId);

    @Query("SELECT r FROM Review r" +
            " WHERE r.orderDetail.product.id = :productId")
    List<Review> findByProduct(int productId, Pageable pageable);
}
