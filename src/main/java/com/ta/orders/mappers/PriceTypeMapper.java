package com.ta.orders.mappers;

import com.ta.orders.dto.PriceTypeDto;
import com.ta.orders.model.PriceType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PriceTypeMapper {

    @Mapping(target = "default", constant = "false")
    @Mapping(target = "creationTime", ignore = true)
    PriceType toEntity(PriceTypeDto dto);

    PriceTypeDto toDto(PriceType entity);
}
