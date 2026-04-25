package com.ta.orders.controller;

import com.ta.orders.dto.ProductCategoryDto;
import com.ta.orders.service.ProductCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product-categories")
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "Product Categories", description = "APIs for managing product categories")
public class ProductCategoriesController {

    private final ProductCategoryService productCategoryService;

    @PostMapping
    @Operation(summary = "Create a new product category", description = "Creates a new product category with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product category created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductCategoryDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ProductCategoryDto createCategory(@RequestBody ProductCategoryDto productCategoryDto) {
        return productCategoryService.create(productCategoryDto);
    }

    @GetMapping
    @Operation(summary = "Get all product categories", description = "Retrieves a list of all available product categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of product categories retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductCategoryDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<ProductCategoryDto> getAllCategories() {
        return productCategoryService.getAll();
    }
}


