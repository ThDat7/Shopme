package com.shopme.common.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass

@Getter @Setter
public abstract class AbstractAddress extends IdBaseEntity {
    @Column(name = "first_name", length = 45, nullable = false)
    protected String firstName;

    @Column(name = "last_name", length = 45, nullable = false)
    protected String lastName;

    @Column(name = "phone_number", length = 15, nullable = false)
    protected String phoneNumber;

    @Column(name = "address_line_1", nullable = false, length = 64)
    protected String addressLine1;

    @Column(name = "address_line_2", length = 64)
    protected String addressLine2;

    @Column(nullable = false, length = 45)
    protected String city;

    @Column(nullable = false, length = 45)
    protected String state;

    @Column(name = "postal_code", length = 10, nullable = false)
    protected String postalCode;
}
