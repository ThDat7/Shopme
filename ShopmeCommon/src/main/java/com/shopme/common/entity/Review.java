package com.shopme.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reviews")

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Review extends IdBaseEntity {
    @Column(length = 128, nullable = false)
    private String headLine;

    @Column(length = 300, nullable = false)
    private String comment;

    private int rating;

    @Column(nullable = false)
    private Date reviewTime;

    @Column(name = "count_like")
    private Integer countLike;

    @OneToOne
    @JoinColumn(name = "order_detail_id")
    private OrderDetail orderDetail;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
