# Orders Desk API Integration Guide for React Admin Panel

This document provides all the necessary information for integrating the Orders Desk backend with a React frontend (Admin Panel).

## 🚀 Getting Started

### API Base URL
- Local: `http://localhost:8080/api/v1`
- Swagger UI (Interactive Docs): `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

### Authentication & CORS
- **CORS**: Enabled for all controllers via `@CrossOrigin`.
- **Auth**: Currently no authentication is implemented (Open API).

---

## 📦 Resource: Products
Manage the food items/products.

### `GET /api/v1/products`
Fetch a paginated list of products.
- **Query Params**:
  - `page` (number, default: 0)
  - `size` (number, default: 100)
  - `category` (optional, ProductCategoryDto)
- **Response**: List of `ProductDto` objects.

### `POST /api/v1/products`
Create a new product.
- **Body**: `ProductDto`
- **Fields**: 
  - `name` (string, required) - Name of the product
  - `description` (string) - Detailed description
  - `categoryId` (number, required) - ID of the product category
  - `imageUrl` (string) - URL to the product image
  - `price` (number) - Current price
- **Response**: Created `ProductDto` object.

---

## 📁 Resource: Product Categories
Group products into categories (e.g., "Main Course", "Drinks").

### `GET /api/v1/product-categories`
List all categories.
- **Cache**: Cached for 5 mins.
- **Response**: Array of `ProductCategoryDto`.

### `POST /api/v1/product-categories`
Create a category.
- **Body**: `ProductCategoryDto`
- **Fields**:
  - `name` (string, required) - Name of the category (e.g., "Desserts")
  - `imageUrl` (string, optional) - URL to the category image
- **Response**: Created `ProductCategoryDto` object.

---

## 💰 Resource: Prices & Price Types
Handle different pricing strategies (e.g., "Regular Price", "Discount Price").

### `GET /api/v1/price_type`
Fetch available price types.
- **Response**: Array of `PriceTypeDto`.

### `POST /api/v1/price_type`
Create a new price type.
- **Body**: `PriceTypeDto` (e.g., `{ "name": "Wholesale" }`)
- **Response**: HTTP 201 with created `PriceTypeDto`.

### `POST /api/v1/price_type/batch`
Create multiple price types in a single request.
- **Body**: Array of `PriceTypeDto`
- **Response**: HTTP 201 with array of created `PriceTypeDto`.

### `POST /api/v1/prices/batch`
Register prices for multiple products at once.
- **Body**: `BatchPriceRequestDto`
- **Structure**:
```json
{
  "priceTypeId": 1,
  "prices": [
    { "productId": 10, "value": 15.50 },
    { "productId": 11, "value": 20.00 }
  ]
}
```
- **Response**: HTTP 200 with array of saved `PriceDto` on success, or HTTP 400 with `BatchPriceValidationResult` on validation failure.

### `GET /api/v1/prices`
Get all prices in the system (including expired prices).
- **Response**: Array of `PriceDto`.

### `GET /api/v1/prices/current`
Get all current active prices.
- **Response**: Array of `PriceDto`.

### `GET /api/v1/prices/current/{priceType}`
Get the latest prices filtered by a specific price type.
- **Path Param**: `priceType` (string) - The price type name (e.g., "RETAIL", "WHOLESALE")
- **Response**: Array of `PriceDto`.

---

## 👥 Resource: Customers
Manage client information.

### `GET /api/v1/customers`
List all customers.
- **Response**: Array of `CustomerDto`.

### `GET /api/v1/customers/{id}`
Get a customer by ID.
- **Path Param**: `id` (number)
- **Response**: `CustomerDto` object.

### `POST /api/v1/customers`
Create a new customer.
- **Body**: `CustomerDto`
- **Fields**:
  - `name` (string, required) - Full name of the customer
  - `email` (string, required) - Email address
  - `phone` (string, required) - Phone number
  - `expeditorId` (number, optional) - ID of the assigned expeditor
- **Response**: HTTP 201 with created `CustomerDto`.

### `PUT /api/v1/customers/{id}`
Update an existing customer.
- **Path Param**: `id` (number)
- **Body**: `CustomerDto`
- **Response**: Updated `CustomerDto` object.

### `DELETE /api/v1/customers/{id}`
Delete a customer.
- **Path Param**: `id` (number)
- **Response**: HTTP 204 No Content.

---

## 🚚 Resource: Expeditors
Manage expeditors (delivery personnel) and their assigned customers.

### `GET /api/v1/expeditors`
List all expeditors.
- **Response**: Array of `ExpeditorDto`.

### `GET /api/v1/expeditors/{id}`
Get an expeditor by ID.
- **Path Param**: `id` (number)
- **Response**: `ExpeditorDto` object.

### `POST /api/v1/expeditors`
Create a new expeditor.
- **Body**: `ExpeditorDto`
- **Fields**: 
  - `name` (string, required) - Full name of the expeditor
  - `phone` (string, required) - Phone number
- **Response**: HTTP 201 with created `ExpeditorDto`.

### `PUT /api/v1/expeditors/{id}`
Update an existing expeditor.
- **Path Param**: `id` (number)
- **Body**: `ExpeditorDto`
- **Response**: Updated `ExpeditorDto` object.

### `DELETE /api/v1/expeditors/{id}`
Delete an expeditor.
- **Path Param**: `id` (number)
- **Response**: HTTP 204 No Content.

### `GET /api/v1/expeditors/{id}/customers`
Get all customers assigned to a specific expeditor.
- **Path Param**: `id` (number) - Expeditor ID
- **Response**: Array of `CustomerDto`.

---

## 📋 Data Models (DTOs)

### `ProductDto`
```typescript
{
  id: number;              // Unique identifier
  name: string;            // Product name
  description: string;     // Product description
  categoryId: number;      // Category ID
  imageUrl: string;        // Product image URL
  price: number;           // Current price
}
```

### `ProductCategoryDto`
```typescript
{
  id: number;              // Unique identifier
  name: string;            // Category name (e.g., "Beverages")
  imageUrl: string;        // Category image URL
}
```

### `CustomerDto`
```typescript
{
  id: number;              // Unique identifier
  name: string;            // Full name
  email: string;           // Email address
  phone: string;           // Phone number
  expeditorId: number;     // Assigned expeditor ID (optional)
  creationTime: string;    // ISO 8601 timestamp
}
```

### `ExpeditorDto`
```typescript
{
  id: number;              // Unique identifier
  name: string;            // Full name
  phone: string;           // Phone number
  creationTime: string;    // ISO 8601 timestamp
}
```

### `PriceTypeDto`
```typescript
{
  id: number;              // Unique identifier
  name: string;            // Type name (e.g., "RETAIL", "WHOLESALE")
}
```

### `PriceDto`
```typescript
{
  id: number;              // Unique identifier
  productId: number;       // Product ID
  priceType: string;       // Price type name
  price: number;           // Price value (BigDecimal)
  validFrom: string;       // ISO 8601 timestamp
  validTo: string;         // ISO 8601 timestamp
  isCurrent: boolean;      // Is this the active price?
}
```

### `BatchPriceRequestDto`
```typescript
{
  priceTypeId: number;     // Price type ID
  prices: PriceItemDto[];  // Array of price items
}
```

### `PriceItemDto`
```typescript
{
  productId: number;       // Product ID
  value: number;           // Price value
}
```

### `BatchPriceValidationResult`
```typescript
{
  valid: boolean;          // Overall validation status
  errors: string[];        // Validation error messages
}
```

---

## 🛠 React Integration Tips

### Recommended Libraries
1. **HTTP Client**: `axios` or `React Query` (highly recommended for caching).
2. **State Management**: `Zustand` or `Context API`.
3. **Form Handling**: `React Hook Form` with `Zod` validation.
4. **UI Components**: `MUI (Material UI)` or `Tailwind UI`.

### Example API Service (Axios)
```javascript
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api/v1'
});

export const getProducts = (page = 0, size = 10) => 
  api.get(`/products?page=${page}&size=${size}`);

export const createProduct = (data) => 
  api.post('/products', data);
```

### Data Models (DTOs)
The backend uses **MapStruct** to ensure DTO consistency. All DTOs are documented in the **Data Models** section above. Ensure your React TypeScript interfaces match these structures.

---

## 📝 API Summary

| Resource | Endpoint | Methods | Description |
|----------|----------|---------|-------------|
| Products | `/api/v1/products` | GET, POST | Manage products with pagination |
| Product Categories | `/api/v1/product-categories` | GET, POST | Manage product categories (cached) |
| Customers | `/api/v1/customers` | GET, GET/:id, POST, PUT/:id, DELETE/:id | Full CRUD for customers |
| Expeditors | `/api/v1/expeditors` | GET, GET/:id, POST, PUT/:id, DELETE/:id | Full CRUD for expeditors |
| Expeditor Customers | `/api/v1/expeditors/{id}/customers` | GET | Get customers by expeditor |
| Price Types | `/api/v1/price_type` | GET, POST, POST/batch | Manage price types |
| Prices | `/api/v1/prices` | GET, POST/batch | Manage product prices |
| Current Prices | `/api/v1/prices/current` | GET | Get all current active prices |
| Prices by Type | `/api/v1/prices/current/{priceType}` | GET | Get current prices by type |

