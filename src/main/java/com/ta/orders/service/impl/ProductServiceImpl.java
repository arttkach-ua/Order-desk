package com.ta.orders.service.impl;

import com.ta.orders.dto.ProductCategoryDto;
import com.ta.orders.dto.ProductDto;
import com.ta.orders.mappers.ProductCategoryMapper;
import com.ta.orders.mappers.ProductMapper;
import com.ta.orders.model.Product;
import com.ta.orders.model.ProductCategory;
import com.ta.orders.repository.ProductRepository;
import com.ta.orders.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductCategoryMapper productCategoryMapper;

    @Override
    public ProductDto create(ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        productRepository.save(product);
        return productMapper.toDto(product);
    }

    @Override
    public List<ProductDto> findAll(ProductCategoryDto productCategoryDto, int page, int size) {
        return getAllFromDb(productCategoryDto, page, size)
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    private List<Product> getAllFromDb(ProductCategoryDto productCategoryDto, int page, int size) {
        if (productCategoryDto != null) {
            ProductCategory category = productCategoryMapper.toEntity(productCategoryDto);
            return productRepository.findAllByCategory(category, PageRequest.of(page, size));
        } else {
            return productRepository.findAll(PageRequest.of(page, size)).getContent();
        }
    }
}
