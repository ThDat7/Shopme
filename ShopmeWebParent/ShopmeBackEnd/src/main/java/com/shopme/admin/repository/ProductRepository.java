package com.shopme.admin.repository;

import com.shopme.common.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByName(String name);

    @Query("UPDATE Product p SET p.enabled = :status WHERE p.id = :id")
    @Modifying
    void updateStatus(int id, boolean status);
}
