package com.ta.orders.service.impl;

import com.ta.orders.dto.ProductCategoryDto;
import com.ta.orders.mappers.ProductCategoryMapper;
import com.ta.orders.model.ProductCategory;
import com.ta.orders.repository.ProductCategoryRepository;
import com.ta.orders.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryMapper productCategoryMapper;

    @Override
    @CacheEvict(value = "productCategories", allEntries = true)
    public ProductCategoryDto create(ProductCategoryDto productCategoryDto) {
        ProductCategory savedEntity = productCategoryRepository.save(productCategoryMapper.toEntity(productCategoryDto));
        return productCategoryMapper.toDto(savedEntity);

    }

    @Override
    @Cacheable(value = "productCategories")
    public List<ProductCategoryDto> getAll() {
        return productCategoryRepository.findAll()
                .stream()
                .map(productCategoryMapper::toDto)
                .toList();
    }
}
