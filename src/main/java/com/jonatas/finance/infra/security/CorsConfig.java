package com.jonatas.finance.infra.security;

import java.util.List;
import java.util.Objects;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("security.cors")
public record CorsConfig(
    String mapping,
    List<String> allowedOrigins,
    List<String> allowedMethods,
    List<String> allowedHeaders,
    boolean allowCredentials) {

  public CorsConfig {
    mapping = Objects.requireNonNullElse(mapping, "/**");
    allowedOrigins = defaultList(allowedOrigins, "*");
    allowedMethods = defaultList(allowedMethods, "*");
    allowedHeaders = defaultList(allowedHeaders, "*");

    validate(allowedOrigins, allowCredentials);
  }

  private List<String> defaultList(List<String> value, String defaultValue) {
    return value == null || value.isEmpty() ? List.of(defaultValue) : value;
  }

  private static void validate(List<String> allowedOrigins, boolean allowCredentials) {
    if (allowCredentials && allowedOrigins.contains("*")) {
      throw new IllegalArgumentException(
          "security.cors.allowed-origins cannot contain '*' when security.cors.allow-credentials is true");
    }
  }

  @Override
  public final String toString() {
    return """
      mapping: %s
      allowed-origins: %s
      allowed-methods: %s
      allowed-headers: %s
      allow-credentials: %s
      """
        .formatted(
            this.mapping,
            String.join(",", this.allowedOrigins),
            String.join(",", this.allowedMethods),
            String.join(",", this.allowedHeaders),
            this.allowCredentials ? "true" : "false");
  }
}
