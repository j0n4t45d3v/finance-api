package br.com.managementfinanceapi.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class PostgreSQLTestContainer {

  @Container
  static final PostgreSQLContainer<?> postgresContainer;

  static {
    postgresContainer = new PostgreSQLContainer<>("postgres:15.12-alpine3.21");
    postgresContainer.withDatabaseName("easy-finance-test");
    postgresContainer.withUsername("test");
    postgresContainer.withPassword("test123");
  }

  @DynamicPropertySource
  static void config(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
    registry.add("spring.flyway.url", postgresContainer::getJdbcUrl);
  }
}
