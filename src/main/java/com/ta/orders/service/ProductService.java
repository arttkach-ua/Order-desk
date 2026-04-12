package com.ta.orders.service;

import com.ta.orders.dto.ProductCategoryDto;
import com.ta.orders.dto.ProductDto;

import java.util.List;

public interface ProductService {
    ProductDto create(ProductDto productDto);
    List<ProductDto> findAll(ProductCategoryDto productCategoryDto, int page, int size);
}
