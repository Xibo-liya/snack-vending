package com.snackvending.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "device")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 32)
    private String deviceNo;

    @Column(nullable = false)
    private Boolean online;

    @Column(length = 32)
    private String status;

    @Column(length = 16)
    private String temperature;

    @Column(length = 128)
    private String message;
}
