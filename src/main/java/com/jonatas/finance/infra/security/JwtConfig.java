package com.jonatas.finance.infra.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("security.jwt")
public record JwtConfig(String issuer, TokenSignatureConfig access, TokenSignatureConfig refresh) {

  public record TokenSignatureConfig(String secret, Long exp) {}

  public String accessSecret() {
    return secret(access);
  }

  public String refreshSecret() {
    return secret(refresh);
  }

  private static String secret(TokenSignatureConfig signature) {
    return signature.secret();
  }
}
