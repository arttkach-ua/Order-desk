package com.ta.orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Customer data transfer object")
public class CustomerDto {
    @Schema(description = "Unique identifier for the customer", example = "1")
    private Long id;

    @Schema(description = "Full name of the customer", example = "John Doe", required = true)
    private String name;

    @Schema(description = "Email address of the customer", example = "john.doe@example.com", required = true)
    private String email;

    @Schema(description = "Phone number of the customer", example = "+1234567890", required = true)
    private String phone;

    @Schema(description = "ID of the assigned expeditor", example = "5")
    private Long expeditorId;

    @Schema(description = "Customer creation timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime creationTime;
}

