package com.ta.orders.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Orders Desk API")
                        .version("1.0.0")
                        .description("REST API for Orders Desk - Food Selling Application with Product Categories, Products, Prices, Price Types, and Customer Management")
                        .contact(new Contact()
                                .name("Orders Desk Team")
                                .url("https://github.com/orders-desk")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server"),
                        new Server()
                                .url("https://api.orders-desk.com")
                                .description("Production Server")
                ));
    }
}

