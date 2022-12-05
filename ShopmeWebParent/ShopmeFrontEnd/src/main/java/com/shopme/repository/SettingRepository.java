package com.shopme.repository;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Integer> {

    @Query("SELECT s FROM Setting s WHERE s.category = :c1 OR s.category = :c2")
    List<Setting> findByTwoCategories(SettingCategory c1, SettingCategory c2);

    List<Setting> findByCategory(SettingCategory category);
}
