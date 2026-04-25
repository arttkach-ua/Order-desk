package com.ta.orders.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "customers")
@Entity
@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Schema(description = "Customer entity")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the customer", example = "1")
    private long id;

    @Column
    @Schema(description = "Full name of the customer", example = "John Doe")
    private String name;

    @Column(name = "created_at", insertable = false, updatable = false)
    @Schema(description = "Customer creation timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime creationTime;

    @Column(name = "email")
    @Schema(description = "Email address of the customer", example = "john.doe@example.com")
    private String email;

    @Column(name = "phone")
    @Schema(description = "Phone number of the customer", example = "+1234567890")
    private String phone;
}


