package br.com.managementfinanceapi.adapter.out.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TokenGenerateTest {

  private TokenGenerate tokenGenerate;

  private static final String ISSUER = "issuer@test.com";
  private static final String SECRET_JWT = "secretIsSecret";
  private static final long EXPIRE_TIME = 240;
  private static final long EXPIRE_TIME_REFRESH_TOKEN = 240;

  private static final String SUBJECT_TEST = "test@test.test";

  @BeforeEach
  void setUp() {
    this.tokenGenerate = new TokenGenerate(ISSUER, SECRET_JWT, EXPIRE_TIME, EXPIRE_TIME_REFRESH_TOKEN);
  }

  @Test
  @DisplayName("should generate access token")
  void shouldGenerateAccessToken() {
    String accessToken = this.tokenGenerate.access(SUBJECT_TEST);
    DecodedJWT decodedJWT = JWT.decode(accessToken);
    assertEquals(ISSUER, decodedJWT.getIssuer());
    assertEquals(SUBJECT_TEST, decodedJWT.getSubject());
    Instant expiresAtAsInstant = decodedJWT
        .getExpiresAtAsInstant()
        .atOffset(ZoneOffset.UTC)
        .toInstant();
    Instant now = LocalDateTime.now()
        .atOffset(ZoneOffset.UTC)
        .toInstant();
    assertTrue(
        expiresAtAsInstant.isAfter(now),
        String.format("expire_at: %s | now: %s", expiresAtAsInstant, now));
  }

  @Test
  @DisplayName("should generate refresh token")
  void shouldGenerateRefreshToken() {
    String refreshToken = this.tokenGenerate.refresh(SUBJECT_TEST);
    DecodedJWT decodedJWT = JWT.decode(refreshToken);
    assertEquals(ISSUER, decodedJWT.getIssuer());
    assertEquals(SUBJECT_TEST, decodedJWT.getSubject());
    Instant expiresAtAsInstant = decodedJWT
        .getExpiresAtAsInstant()
        .atOffset(ZoneOffset.UTC)
        .toInstant();
    Instant now = LocalDateTime.now()
        .atOffset(ZoneOffset.UTC)
        .toInstant();
    assertTrue(
        expiresAtAsInstant.isAfter(now),
        String.format("expire_at: %s | now: %s", expiresAtAsInstant, now));
  }
}