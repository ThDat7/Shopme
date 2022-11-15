package com.shopme.admin.repository;

import com.shopme.common.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByName(String name);

    boolean existsByAlias(String alias);

    @Query("SELECT c FROM Category c WHERE c.parent IS NULL")
    List<Category> getParent();
}
