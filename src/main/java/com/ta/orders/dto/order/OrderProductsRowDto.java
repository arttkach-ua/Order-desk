package com.ta.orders.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Raw of order document")
public record OrderProductsRowDto(
        @Schema(description = "Unique identifier for the price type", example = "1")
        long id,
        @Schema(description = "Name of the price type (e.g., RETAIL, WHOLESALE, BULK)", example = "RETAIL")
        String name) {

}
