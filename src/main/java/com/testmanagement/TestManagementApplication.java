package com.testmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for Test Management API
 * Handles test execution management and dashboard analytics
 */
@SpringBootApplication
@EnableScheduling
public class TestManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestManagementApplication.class, args);
    }
}
