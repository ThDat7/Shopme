package com.shopme.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "addresses")

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Address extends AbstractAddressWithCountry {

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "default_address")
    private boolean defaultForShipping;


}
