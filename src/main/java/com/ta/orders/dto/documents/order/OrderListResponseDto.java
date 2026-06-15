package com.ta.orders.dto.documents.order;

import com.ta.orders.dto.ReferenceDto;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderListResponseDto(
        long id,
        Instant date,
        Integer number,
        ReferenceDto client,
        ReferenceDto priceType,
        BigDecimal sum,
        String comment
) {
}
