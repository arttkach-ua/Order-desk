package com.ta.orders.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Table(name = "product_categories")
@Entity
@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime creationTime;

    @Column(name = "image_url")
    private String imageUrl;
}
