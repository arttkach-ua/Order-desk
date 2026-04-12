package com.ta.orders.mappers;

import com.ta.orders.dto.ProductDto;
import com.ta.orders.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "categoryId", target = "category.id")
    Product toEntity(ProductDto dto);
    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toDto(Product entity);
}
