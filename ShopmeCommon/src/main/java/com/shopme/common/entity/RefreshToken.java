package com.shopme.common.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity(name = "refresh_token")
@Getter @Setter
public class RefreshToken {
    @Id
    @Column(name = "user_id")
    private int userId;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    public RefreshToken() {
    }
}
