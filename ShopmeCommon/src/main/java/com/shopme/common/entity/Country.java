package com.shopme.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "countries")

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Country extends IdBaseEntity{
    @Column(length = 45, nullable = false)
    private String name;

    @Column(length = 5, nullable = false)
    private String code;

    @OneToMany(mappedBy = "country")
    private Set<State> states;


}
