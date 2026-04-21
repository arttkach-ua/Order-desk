package com.ta.orders.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "price_history")
@Entity
@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "product_id")
    private long productId;

    @Column(name = "price_type")
    private String priceType;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "valid_from")
    private LocalDateTime validFrom;

    @Column(name = "valid_to")
    private LocalDateTime validTo;

    @Column(name = "is_current")
    private boolean isCurrent;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}

