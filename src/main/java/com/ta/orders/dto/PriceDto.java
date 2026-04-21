package com.ta.orders.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PriceDto {
    private long id;
    private long productId;
    private String priceType;
    private BigDecimal price;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private boolean isCurrent;
}

