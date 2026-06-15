package com.ta.orders.dto.documents.order;

import com.ta.orders.dto.documents.order.rows.OrderProductsRowRequestDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderRequestDto(
        long clientId,
        Instant date,
        long priceTypeId,
        String comments,
        BigDecimal sum,
        List<OrderProductsRowRequestDto> products
) {
}
