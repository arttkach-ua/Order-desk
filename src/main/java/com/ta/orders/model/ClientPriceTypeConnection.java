package com.ta.orders.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Table(name = "client_price_type_connection")
@Entity
@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class ClientPriceTypeConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "client_id")
    private long clientId;

    @Column(name = "price_type_id")
    private long priceTypeId;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime creationTime;
}

