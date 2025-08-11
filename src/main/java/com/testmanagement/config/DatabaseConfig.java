package com.testmanagement.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Database configuration for JPA repositories
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.testmanagement.repository")
public class DatabaseConfig {
    // Additional database configurations can be added here if needed
}
