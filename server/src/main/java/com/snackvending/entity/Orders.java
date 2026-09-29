package com.snackvending.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Orders {
    @Id
    @Column(length = 64)
    private String orderNo;

    @Column(nullable = false)
    private Double totalAmount;

    @Column(nullable = false)
    private Integer totalCount;

    @Column(length = 32)
    private String status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createTime;

    private LocalDateTime payTime;

    @Column(length = 2048)
    private String itemsJson;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
}
