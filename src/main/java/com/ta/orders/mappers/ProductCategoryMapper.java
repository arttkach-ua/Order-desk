package com.ta.orders.mappers;

import com.ta.orders.dto.ProductCategoryDto;
import com.ta.orders.model.ProductCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductCategoryMapper {
    ProductCategory toEntity(ProductCategoryDto dto);
    ProductCategoryDto toDto(ProductCategory entity);
}
