# Design.md

## Overview

This application is an admin panel for a food ordering system built with Spring Boot 3.3.4. It provides a comprehensive backend API for managing products, categories, pricing strategies, customers, and delivery personnel (expeditors). The system follows a layered architecture pattern with RESTful APIs, uses PostgreSQL for data persistence, and implements caching for optimal performance.

**Technology Stack**: Spring Boot, PostgreSQL, Liquibase, MapStruct, Caffeine Cache, Lombok, JPA/Hibernate

## Components

### 1. Product Management
Manage food items with categories, descriptions, and images.

**API Endpoint**: `/api/v1/products`

**Key Features**:
- List products with pagination
- Create new products
- Update existing products
- View product details
- Assign products to categories

**DTO Example** (`ProductDto`):
```json
{
  "id": 1,
  "name": "Espresso Coffee",
  "description": "Premium Italian espresso beans",
  "categoryId": 2,
  "imageUrl": "https://example.com/images/espresso.jpg",
  "price": 3.50
}
```

### 2. Product Category Management
Group products into categories (e.g., "Main Course", "Drinks", "Desserts").

**API Endpoint**: `/api/v1/product-categories`

**Key Features**:
- List all categories
- Create new categories
- Update existing categories
- Add category images
- View category details

**DTO Example** (`ProductCategoryDto`):
```json
{
  "id": 1,
  "name": "Beverages",
  "imageUrl": "https://example.com/images/beverages.jpg"
}
```

### 3. Price Type Management
Define and manage different pricing strategies.

**API Endpoint**: `/api/v1/price-types`

**Key Features**:
- List all price types
- Create new price types (e.g., RETAIL, WHOLESALE, BULK)
- View price type details

**DTO Example** (`PriceTypeDto`):
```json
{
  "id": 1,
  "name": "RETAIL"
}
```

### 4. Price Management
Handle multiple pricing strategies with temporal validity and historical tracking.

**API Endpoint**: `/api/v1/prices`

**Key Features**:
- Set prices for products by type
- Batch update prices for multiple products
- View current active prices
- View price history with validity periods
- Track price changes over time

**DTO Example** (`PriceDto`):
```json
{
  "id": 1,
  "productId": 10,
  "priceType": "RETAIL",
  "price": 4.99,
  "validFrom": "2024-01-01T00:00:00",
  "validTo": "2024-12-31T23:59:59",
  "isCurrent": true
}
```

**Batch Price Request DTO Example** (`BatchPriceRequestDto`):
```json
{
  "priceType": "RETAIL",
  "validFrom": "2024-01-01T00:00:00",
  "prices": [
    {
      "productId": 5,
      "price": 12.99
    },
    {
      "productId": 8,
      "price": 8.50
    }
  ]
}
```

**Price Item DTO Example** (`PriceItemDto`):
```json
{
  "productId": 5,
  "price": 12.99
}
```

### 5. Customer Management
Manage customer information and assignments to expeditors.

**API Endpoint**: `/api/v1/customers`

**Key Features**:
- Full CRUD operations for customers
- Assign customers to expeditors
- View customer details with timestamps
- Track customer creation time
- Manage customer contact information

**DTO Example** (`CustomerDto`):
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phone": "+1234567890",
  "expeditorId": 5,
  "creationTime": "2024-01-15T10:30:00"
}
```

### 6. Expeditor Management
Manage delivery personnel and their assigned customers.

**API Endpoint**: `/api/v1/expeditors`

**Key Features**:
- Full CRUD operations for expeditors
- View expeditor's assigned customers
- Track customer assignments
- Manage expeditor contact information
- Track expeditor creation time

**DTO Example** (`ExpeditorDto`):
```json
{
  "id": 1,
  "name": "Ivan Petrov",
  "phone": "+380501234567",
  "creationTime": "2024-01-15T10:30:00"
}
```

## API Versioning

All API endpoints use the `/api/v1/` prefix for version control, ensuring backward compatibility for future updates.
