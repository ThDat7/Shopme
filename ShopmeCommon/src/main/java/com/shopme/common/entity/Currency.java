package com.shopme.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "currency")

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Currency extends IdBaseEntity {
    @Column(length = 64, nullable = false)
    private String name;

    @Column(length = 3, nullable = false)
    private String symbol;

    @Column(nullable = false, length = 4)
    private String code;

}
