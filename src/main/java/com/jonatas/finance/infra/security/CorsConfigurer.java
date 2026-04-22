package com.jonatas.finance.infra.security;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class CorsConfigurer implements WebMvcConfigurer{

  private static final Logger log = LoggerFactory.getLogger(CorsConfigurer.class);

  private final String mapping;
  private final List<String> allowedOrigins;
  private final List<String> allowedMethods;
  private final List<String> allowedHeaders;
  private final Boolean allowCredentials;

  public CorsConfigurer(
    @Value("${security.cors.mapping}") String mapping,
    @Value("${security.cors.allowed-origins}") String allowedOrigins,
    @Value("${security.cors.allowed-methods}") String allowedMethods,
    @Value("${security.cors.allowed-headers}") String allowedHeaders,
    @Value("${security.cors.allow-credentials}") Boolean allowCredentials
  ) {
    this.mapping = Objects.requireNonNullElse(mapping, "/**");
    this.allowedOrigins = Arrays.asList(Objects.requireNonNullElse(allowedOrigins,"*").split(","));
    this.allowedMethods = Arrays.asList(Objects.requireNonNullElse(allowedMethods, "*").split(","));
    this.allowedHeaders = Arrays.asList(Objects.requireNonNullElse(allowedHeaders, "*").split(","));
    this.allowCredentials = Objects.requireNonNullElse(allowCredentials, Boolean.FALSE);
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    log.info("Cors configuration:\n{}", this);
    if (this.allowedOrigins.contains("*") && this.allowCredentials) {
      log.error("Invalid Cors Configuration allowed-origins cannot be * and allow-credentials be true");
      return;
    }
    registry
    .addMapping(this.mapping)
    .allowedOrigins(this.allowedOrigins.toArray(new String[0]))
    .allowedMethods(this.allowedMethods.toArray(new String[0]))
    .allowedHeaders(this.allowedHeaders.toArray(new String[0]))
    .allowCredentials(this.allowCredentials);
  }

  @Override
  public String toString() {
    return """
      mapping: %s
      allowed-origins: %s
      allowed-methods: %s
      allowed-headers: %s
      allow-credentials: %s
      """.formatted(
        this.mapping,
        String.join(",", this.allowedOrigins),
        String.join(",", this.allowedMethods),
        String.join(",", this.allowedHeaders),
        this.allowCredentials.toString()
      );
  }

}
