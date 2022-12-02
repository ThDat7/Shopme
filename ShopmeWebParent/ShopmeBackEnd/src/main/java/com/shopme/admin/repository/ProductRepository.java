package com.shopme.admin.repository;

import com.shopme.common.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>,
                                            JpaSpecificationExecutor<Product> {

    boolean existsByName(String name);

    @Query("UPDATE Product p SET p.enabled = :status WHERE p.id = :id")
    @Modifying
    void updateStatus(int id, boolean status);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% " +
            "OR p.shortDescription LIKE %:keyword% " +
            "OR p.fullDescription LIKE %:keyword% " +
            "OR p.brand.name LIKE %:keyword% " +
            "OR p.category.name LIKE %:keyword%")
    Page<Product> findAll(String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.category.id = ?1 " +
            "OR p.category.allParentIds LIKE %:categoryIdMatch%)")
    Page<Product> findAllInCategory(int categoryId, String categoryIdMatch,
                                    Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.category.id = ?1 " +
            "OR p.category.allParentIds LIKE %:categoryIdMatch%) AND " +
            "(p.name LIKE %:keyword% " +
            "OR p.shortDescription LIKE %:keyword% " +
            "OR p.fullDescription LIKE %:keyword% " +
            "OR p.brand.name LIKE %:keyword% " +
            "OR p.category.name LIKE %:keyword%)")
    Page<Product> searchInCategory(int categoryId, String categoryIdMatch,
                                          String keyword, Pageable pageable);
}
