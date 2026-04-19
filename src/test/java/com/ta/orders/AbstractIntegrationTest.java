package com.ta.orders;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Abstract base class for integration tests that require a PostgreSQL database.
 * Uses Testcontainers to provide a singleton PostgreSQL 15 container for all integration tests.
 * The container is started once and reused across all test classes that extend this base class.
 */
@SpringBootTest
@Testcontainers
public abstract class AbstractIntegrationTest {

    /**
     * Singleton PostgreSQL container using postgres:15 to match production environment.
     * Static field ensures the container is started once per test suite run for performance.
     */
    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    /**
     * Dynamically overrides Spring datasource properties to connect to the Testcontainers PostgreSQL instance.
     * This ensures integration tests use the containerized database instead of the default configuration.
     */
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }
}

