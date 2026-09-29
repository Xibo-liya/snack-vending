package com.snackvending.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_no")
    private Orders order;

    private Long productId;

    @Column(length = 64)
    private String productName;

    private Double price;

    @Column(length = 32)
    private String image;

    private Integer quantity;
}
