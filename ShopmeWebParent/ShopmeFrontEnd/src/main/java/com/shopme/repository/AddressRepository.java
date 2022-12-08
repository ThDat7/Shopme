package com.shopme.repository;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    List<Address> findByCustomer(Customer customer);

    @Query("SELECT a FROM Address a WHERE a.id = :id " +
            "AND a.customer = :customer")
    Optional<Address> findByIdAndCustomer(int id, Customer customer);

    @Modifying
    @Query("DELETE FROM Address a WHERE a.id = :id " +
            "AND a.customer = :customer")
    void  deleteByIdAndCustomer(int id, Customer customer);

    int countByCustomer(Customer customer);

    @Modifying
    @Query("UPDATE Address a SET a.defaultForShipping = true " +
            "WHERE a.id = :id AND a.customer = :customer")
    void setDefault(int id, Customer customer);

    @Modifying
    @Query("UPDATE Address a SET a.defaultForShipping = false " +
            "WHERE a.customer = :customer AND a.id <> :defaultId")
    void setNoneDefaultForOther(int defaultId, Customer customer);
}
