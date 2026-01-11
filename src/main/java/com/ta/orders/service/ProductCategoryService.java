package com.ta.orders.service;

import com.ta.orders.dto.ProductCategoryDto;

import java.util.List;

public interface ProductCategoryService {
    ProductCategoryDto create(ProductCategoryDto productCategoryDto);

    List<ProductCategoryDto> getAll();
}
