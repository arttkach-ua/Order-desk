package com.ta.orders.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PriceItemDto {
    private long productId;
    private BigDecimal price;
}

