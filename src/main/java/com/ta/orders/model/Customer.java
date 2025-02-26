package com.ta.orders.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "customers")
@Entity
@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime creationTime;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;
}
