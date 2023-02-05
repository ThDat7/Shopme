package com.shopme.admin.repository;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Integer>,
                                         JpaSpecificationExecutor<Brand> {
    boolean existsByName(String name);

    @Query("SELECT b FROM Brand b WHERE b.name LIKE %:keywordSearch%")
    Page<Brand> search(String keywordSearch, Pageable pageable);

    @Query("SELECT NEW Brand(b.id, b.name) FROM Brand b WHERE b.id = :id ORDER BY b.name ASC")
    List<Category> getCategoriesById(int id);
}
