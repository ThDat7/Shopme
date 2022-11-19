package com.shopme.admin.repository;

import com.shopme.common.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
    boolean existsByName(String name);

    @Query("SELECT b FROM Brand b WHERE b.name LIKE %:keywordSearch%")
    Page<Brand> search(String keywordSearch, Pageable pageable);
}
