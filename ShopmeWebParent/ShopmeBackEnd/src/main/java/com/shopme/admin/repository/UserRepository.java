package com.shopme.admin.repository;

import com.shopme.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Integer>,
        JpaSpecificationExecutor<User> {
    @Modifying
    void deleteByEmail(String email);

    @Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.email, ' '" +
            ", u.firstName, ' ', u.lastName) LIKE %:keyword%")
    List<User> searchByKeyword(String keyword);

    Optional<User> findByEmail(String s);

    @Query("UPDATE User u SET u.enabled = :status WHERE u.id = :id")
    @Modifying
    void updateStatus(Integer id, Boolean status);

    boolean existsByEmail(String email);

    @Query("SELECT u.password FROM User u WHERE u.email = :email")
    String getPasswordByEmail(String email);

    @Query("SELECT u.id FROM User u WHERE u.email = :email")
    int getIdByEmail(String email);
}
