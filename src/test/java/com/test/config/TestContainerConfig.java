package com.test.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class TestContainerConfig {
    
    @Container
    @ServiceConnection
    static MySQLContainer<?> mysqlContainer = new MySQLContainer<>(
        DockerImageName.parse("mysql:8.0")
    ).withDatabaseName("test_management")
     .withUsername("test")
     .withPassword("test")
     .withInitScript("schema.sql");
}