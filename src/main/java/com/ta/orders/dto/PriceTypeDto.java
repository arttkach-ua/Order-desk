package com.ta.orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Price Type DTO")
public record PriceTypeDto(
        @Schema(description = "Unique identifier for the price type", example = "1")
        long id,
        @Schema(description = "Name of the price type (e.g., RETAIL, WHOLESALE, BULK)", example = "RETAIL")
        String name) {
}
