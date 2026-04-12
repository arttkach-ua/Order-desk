package com.ta.orders.dto;

import lombok.Data;

@Data
public class ProductDto {
    private long id;
    private String name;
    private String description;
    private long categoryId;
    private String imageUrl;
    double price;
}
