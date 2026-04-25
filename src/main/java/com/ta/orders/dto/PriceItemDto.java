package com.ta.orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Price Item DTO for batch pricing")
public class PriceItemDto {
    @Schema(description = "ID of the product", example = "5")
    private long productId;
    @Schema(description = "Price value for this product", example = "12.99")
    private BigDecimal price;
}



