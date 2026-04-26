package com.ta.orders.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "expeditors")
@Entity
@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Schema(description = "Expeditor entity")
public class Expeditor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the expeditor", example = "1")
    private long id;

    @Column(name = "name")
    @Schema(description = "Full name of the expeditor", example = "Ivan Petrov")
    private String name;

    @Column(name = "phone")
    @Schema(description = "Phone number of the expeditor", example = "+380501234567")
    private String phone;

    @Column(name = "created_at", insertable = false, updatable = false)
    @Schema(description = "Expeditor creation timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime creationTime;
}

