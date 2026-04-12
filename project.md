# Food Selling App

## Overview

This project is a food-selling application consisting of three main parts:

- **Backend**: A monolithic Spring Boot application serving as the core API and business logic. Should be be multi module on java.
- **Admin Site**: A web-based interface for administrators to manage the system.
- **Telegram Mini App**: A mini application within Telegram for clients to browse and place orders.

## Business Catalogs

The application manages the following business entities:

- Products
- Product Types
- Clients
- Users
- Price Types
- Forwarder (e.g., delivery partners or suppliers)
- Units of Measurement

## Features

- **Order Placement**: Clients can place orders through the Telegram mini app, following a classic e-commerce flow (e.g., add to cart, checkout, payment).
- **Client-Price Association**: Each client can be linked to a specific price type, allowing for customized pricing.
- **Order History**: Clients can view their order history within the mini app.
- **Product Filters**: The mini app includes filters for:
  - Favorite products
  - Promotional products
  - Product price
  - Product category

## Technologies

The backend uses the following technologies:

- Spring Boot
- Spring Data
- Spring Security
- Liquibase (for database migrations)
- MySQL (database)
- Grafana (monitoring and visualization)
- Docker (containerization)
- Cucumber (BDD testing)
- Karate (API testing)
