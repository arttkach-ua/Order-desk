package com.ta.orders.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BatchPriceRequestDto {
    private String priceType;
    private LocalDateTime validFrom;
    private List<PriceItemDto> prices;
}

