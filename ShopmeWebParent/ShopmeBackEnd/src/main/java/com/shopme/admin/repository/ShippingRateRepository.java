package com.shopme.admin.repository;

import com.shopme.common.entity.ShippingRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingRateRepository extends JpaRepository<ShippingRate, Integer> {
    @Modifying
    @Query("UPDATE ShippingRate s SET s.codSupported = :isSupport " +
            "WHERE s.id = :id")
    void updateCODSupport(int id, boolean isSupport);
}
