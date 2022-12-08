package com.shopme.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "cart_items")

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CartItem extends IdBaseEntity {

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    public CartItem(int productId, int customerId) {
        this.product = new Product(productId);
        this.customer = new Customer(customerId);
    }
}
