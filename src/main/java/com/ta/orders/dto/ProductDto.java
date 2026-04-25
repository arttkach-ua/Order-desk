package com.ta.orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Product DTO")
public class ProductDto {
    @Schema(description = "Unique identifier for the product", example = "1")
    private long id;
    @Schema(description = "Name of the product", example = "Espresso Coffee")
    private String name;
    @Schema(description = "Detailed description of the product", example = "Premium Italian espresso beans")
    private String description;
    @Schema(description = "ID of the product category", example = "2")
    private long categoryId;
    @Schema(description = "URL to the product image", example = "https://example.com/images/espresso.jpg")
    private String imageUrl;
    @Schema(description = "Current price of the product", example = "3.50")
    double price;
}


