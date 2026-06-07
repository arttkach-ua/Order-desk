package com.ta.orders.dto.order.raws;

import com.ta.orders.dto.ReferenceDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Raw of order document")
public record OrderProductsRowResponseDto(
        long id,
        int rowNumber,
        ReferenceDto product,
        long price,
        long count,
        long sum
        ) {
}
