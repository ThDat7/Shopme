package com.shopme.common.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "customer_user_refresh_token")

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CustomerUserRefreshToken {
    @Id
    @Column(name = "user_id")
    private int userId;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;
}
