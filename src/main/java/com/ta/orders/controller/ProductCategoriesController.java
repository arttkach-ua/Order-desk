package com.ta.orders.controller;

import com.ta.orders.dto.ProductCategoryDto;
import com.ta.orders.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product-categories")
@CrossOrigin
@RequiredArgsConstructor
public class ProductCategoriesController {

    private final ProductCategoryService productCategoryService;

    @PostMapping
    public ProductCategoryDto createCategory(@RequestBody ProductCategoryDto productCategoryDto) {
        return productCategoryService.create(productCategoryDto);
    }

    @GetMapping
    public List<ProductCategoryDto> getAllCategories() {
        return productCategoryService.getAll();
    }
}
