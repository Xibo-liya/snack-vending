package com.snackvending.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "category")
public class Category {
    @Id
    @Column(length = 32)
    private String id;

    @Column(nullable = false, length = 32)
    private String name;

    @Column(length = 32)
    private String icon;

    private Integer sortOrder;
}
