package com.ta.orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "Batch Price Request DTO for bulk price registration")
public class BatchPriceRequestDto {
    @Schema(description = "Type of price being registered (e.g., RETAIL, WHOLESALE)", example = "RETAIL")
    private String priceType;
    @Schema(description = "Start date and time for price validity", example = "2024-01-01T00:00:00")
    private LocalDateTime validFrom;
    @Schema(description = "List of product price items to register")
    private List<PriceItemDto> prices;
}



