package com.shopme.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "shipping_rates")

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ShippingRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private float rate;

    private int days;

    @Column(name = "cod_supported")
    private boolean codSupported;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(nullable = false, length = 45)
    private String state;
}
