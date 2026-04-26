package com.ta.orders.mappers;

import com.ta.orders.dto.ExpeditorDto;
import com.ta.orders.model.Expeditor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpeditorMapper {
    @Mapping(target = "creationTime", ignore = true)
    Expeditor toEntity(ExpeditorDto dto);

    ExpeditorDto toDto(Expeditor entity);
}

