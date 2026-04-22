package com.ta.orders.mappers;

import com.ta.orders.dto.PriceDto;
import com.ta.orders.model.Price;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PriceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Price toEntity(PriceDto dto);

    PriceDto toDto(Price entity);
}

