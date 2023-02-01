package com.shopme.common.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "product_images")

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProductImage extends IdBaseEntity{

    private String name;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    public ProductImage(int id, String name, Product product) {
        this.id = id;
        this.name = name;
        this.product = product;
    }


}
