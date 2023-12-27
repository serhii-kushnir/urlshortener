package ua.shortener.service.config;

import org.testcontainers.containers.PostgreSQLContainer;

import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import ua.shortener.service.containers.PostgresTestContainer;

@Testcontainers
public class ContainersEnvironment {

    @Container
    public static PostgreSQLContainer postgreSQLContainer = PostgresTestContainer.getInstance();
}