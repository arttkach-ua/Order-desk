package com.ta.orders.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Table(name = "price_types")
@Entity
@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class PriceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime creationTime;
}
