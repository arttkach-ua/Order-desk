package com.ta.orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Expeditor data transfer object")
public class ExpeditorDto {
    @Schema(description = "Unique identifier for the expeditor", example = "1")
    private Long id;

    @Schema(description = "Full name of the expeditor", example = "Ivan Petrov", required = true)
    private String name;

    @Schema(description = "Phone number of the expeditor", example = "+380501234567", required = true)
    private String phone;

    @Schema(description = "Expeditor creation timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime creationTime;
}

