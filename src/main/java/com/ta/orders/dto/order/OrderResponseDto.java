package com.ta.orders.dto.order;

import com.ta.orders.dto.ReferenceDto;
import com.ta.orders.dto.order.rows.OrderProductsRowResponseDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponseDto(
        long id,
        Instant date,
        Integer number,
        ReferenceDto client,
        ReferenceDto priceType,
        BigDecimal sum,
        String comment,
        List<OrderProductsRowResponseDto> products) {
}
