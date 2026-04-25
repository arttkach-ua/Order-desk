package com.ta.orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "Price DTO")
public class PriceDto {
    @Schema(description = "Unique identifier for the price", example = "1")
    private long id;
    @Schema(description = "ID of the product this price applies to", example = "10")
    private long productId;
    @Schema(description = "Type/category of the price (e.g., RETAIL, WHOLESALE)", example = "RETAIL")
    private String priceType;
    @Schema(description = "Price value", example = "4.99")
    private BigDecimal price;
    @Schema(description = "Price validity start date and time", example = "2024-01-01T00:00:00")
    private LocalDateTime validFrom;
    @Schema(description = "Price validity end date and time", example = "2024-12-31T23:59:59")
    private LocalDateTime validTo;
    @Schema(description = "Indicates if this is the current active price", example = "true")
    private boolean isCurrent;
}



