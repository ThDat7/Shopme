package com.shopme.common.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Table(name = "brands")
public class Brand extends IdBaseEntity {
    @Column(nullable = false, length = 45, unique = true)
    private String name;

    @Column(nullable = false, length = 128)
    private String logo;

    @ManyToMany
    @JoinTable(
            name = "brands_categories",
            joinColumns = @JoinColumn(name = "brand_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @JsonManagedReference
    private Set<Category> categories = new HashSet<>();

    public Brand(String name) {
        this.name = name;
        this.logo = "brand-logo.png";
    }

    public Brand(Integer id) {
        this.id = id;
    }

    public Brand(String name, Set<Category> categories) {
        this.name = name;
        this.logo = "brand-logo.png";
        this.categories = categories;
    }

    public Brand(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
