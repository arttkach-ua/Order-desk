package com.ta.orders.mappers;

import com.ta.orders.dto.ProductCategoryDto;
import com.ta.orders.model.ProductCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductCategoryMapper {
    @Mapping(target = "creationTime", ignore = true)
    ProductCategory toEntity(ProductCategoryDto dto);
    ProductCategoryDto toDto(ProductCategory entity);
}
