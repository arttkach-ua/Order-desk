package com.ta.orders.dto.documents.order.rows;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Raw of order document")
public record OrderProductsRowRequestDto(
        int rowNumber,
        long productId,
        BigDecimal price,
        BigDecimal count,
        BigDecimal sum
        ) {
}
