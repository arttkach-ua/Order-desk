package com.ta.orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Product Category DTO")
public class ProductCategoryDto {
    @Schema(description = "Unique identifier for the product category", example = "1")
    private long id;
    @Schema(description = "Name of the product category", example = "Beverages")
    private String name;
    @Schema(description = "URL to the category image", example = "https://example.com/images/beverages.jpg")
    private String imageUrl;
}


