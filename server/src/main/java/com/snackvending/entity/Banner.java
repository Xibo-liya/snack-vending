package com.snackvending.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "banner")
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String title;

    @Column(length = 256)
    private String description;

    @Column(length = 128)
    private String bgColor;

    @Column(length = 32)
    private String icon;

    private Integer sortOrder;
}
