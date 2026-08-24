package com.jonatas.finance.integrationTest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = true)
public class PostgresSQLContainerConfig {

  @Bean
  @ServiceConnection
  PostgreSQLContainer postgres(@Value("${testcontainers.postgres.image}") String pgImage) {
    return new PostgreSQLContainer(pgImage)
        .withDatabaseName("test-database")
        .withUsername("test")
        .withPassword("test");
  }
}
