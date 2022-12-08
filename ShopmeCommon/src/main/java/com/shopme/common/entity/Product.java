package com.shopme.common.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "products")

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Product extends IdBaseEntity{
    @Column(nullable = false, unique = true, length = 256)
    private String name;

    @Column(nullable = false, unique = true, length = 256)
    private String alias;

    @Column(name = "short_description", nullable = false, length = 512)
    private String shortDescription;

    @Column(name = "full_description", nullable = false, length = 4096)
    private String fullDescription;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "update_time")
    private Date updatedTime;

    private boolean enabled;

    @Column(name = "in_stock")
    private boolean inStock;

    private float cost;

    private float price;

    @Column(name = "discount_percent")
    private float discountPercent;

    private float length;
    private float width;
    private float height;
    private float weight;

    @Column(name = "main_image", nullable = false)
    private String mainImage;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<ProductImage> images = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ProductDetail> details = new ArrayList<>();

    public void addExtraImage(String imageName) {
        this.images.add(new ProductImage(imageName, this));
    }

    public void addDetail(String name, String value) {
        this.details.add(new ProductDetail(name, value, this));
        new ProductDetail(name, value, this);
    }

    public Product(int id) {
        this.id = id;
    }
}
