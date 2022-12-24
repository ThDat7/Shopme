package com.shopme.repository;

import com.shopme.common.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.enabled = true " +
            "AND (p.category.id = :categoryId OR p.category.allParentIds LIKE %:categoryIdMatch%)" +
            "ORDER BY p.name ASC")
    Page<Product> listByCategory(Integer categoryId, String categoryIdMatch,
                                 Pageable pageable);


    @Query("UPDATE Product p SET p.avgRating = COALESCE(" +
            "(SELECT AVG(r.rating) FROM Review r" +
            " WHERE r.orderDetail.product.id = :productId), 0), " +
            " p.reviewCount = (SELECT COUNT(r.id) FROM Review r" +
            " WHERE r.orderDetail.product.id = :productId)" +
            " WHERE p.id = :productId")
    @Modifying
    void updateAvgRatingAndReviewCount(Integer productId);
}
