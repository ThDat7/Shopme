package com.shopme.admin.repository;

import com.shopme.common.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>,
                                            JpaSpecificationExecutor<Customer> {
    @Query("UPDATE Customer c SET c.enabled = :status " +
            "WHERE c.id = :id")
    @Modifying
    void updateStatus(Integer id, boolean status);

    boolean existsByEmail(String email);
}
