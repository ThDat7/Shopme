package com.shopme.repository;

import com.shopme.common.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    boolean existsByEmail(String email);

    Optional<Customer> findByVerificationCode(String verificationCode);

    @Query("UPDATE Customer c SET c.enabled = true, c.verificationCode = null " +
            "WHERE c.id = :id")
    @Modifying
    void enable(Integer id);

    Optional<Customer> findByEmail(String email);

    @Query("SELECT c.id FROM Customer c WHERE c.email = :username")
    int getIdByUsername(String username);
}
