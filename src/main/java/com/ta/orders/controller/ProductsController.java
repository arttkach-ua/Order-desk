package com.ta.orders.controller;

import com.ta.orders.dto.ProductCategoryDto;
import com.ta.orders.dto.ProductDto;
import com.ta.orders.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin
@RequiredArgsConstructor
public class ProductsController {

    private final ProductService productService;

    @GetMapping
    List<ProductDto> getProducts(@RequestParam (required = false) ProductCategoryDto category,
                                 @RequestParam (defaultValue = "0") int page,
                                 @RequestParam (defaultValue = "100") int size) {
        return productService.findAll(category, page, size);
    }

    @PostMapping
    ProductDto createProduct(@RequestBody ProductDto productDto) {
        return productService.create(productDto);
    }
}
