package br.com.managementfinanceapi.adapter.out.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class TokenReaderTest {

  private static final String ISSUER = "test@test.com";
  private static final String SECRET = "secret";
  private static final long EXPIRE_TIME = 60;
  private static final long EXPIRE_TIME_REFRESH_TOKEN = 240;
  private static final String SUBJECT = "john@doe.test";

  private TokenReader tokenReader;
  private String validAccessToken;
  private LocalDateTime expiredAtAccessToken;
  private String validRefreshToken;
  private LocalDateTime expiredAtRefreshToken;

  @BeforeEach
  void setUp() {
    this.tokenReader = new TokenReader();
    Instant instantAccessToken = this.getInstant(EXPIRE_TIME);
    this.expiredAtAccessToken =
        LocalDateTime.ofInstant(instantAccessToken, ZoneOffset.UTC.normalized());
    this.validAccessToken = this.createToken(instantAccessToken);
    Instant instantRefreshToken = this.getInstant(EXPIRE_TIME_REFRESH_TOKEN);
    this.expiredAtRefreshToken =
        LocalDateTime.ofInstant(instantRefreshToken, ZoneOffset.UTC.normalized());
    this.validRefreshToken = this.createToken(instantRefreshToken);
  }

  @Test
  @DisplayName("should return false when 'expired at' is after current time ")
  void shouldReturnFalseWhenExpiredAtIsAfterCurrentTime() {
    boolean isExpiredAccessToken = this.tokenReader.isExpired(this.validAccessToken);
    boolean isExpiredRefreshToken = this.tokenReader.isExpired(this.validRefreshToken);
    assertFalse(isExpiredAccessToken, "Access Token: " + this.validAccessToken);
    assertFalse(isExpiredRefreshToken, "Refresh Token: " + this.validRefreshToken);
  }

  @Test
  @DisplayName("should return token subject")
  void shouldReturnTokenSubject() {
    String subject = this.tokenReader.getSubject(this.validAccessToken);
    assertEquals(SUBJECT, subject);
  }

  @Test
  @DisplayName("should return token expired at")
  void shouldReturnTokenExpiredAt() {
    LocalDateTime expiredAtAccess = this.tokenReader.getExpiredAt(this.validAccessToken);
    assertEquals(this.expiredAtAccessToken.truncatedTo(ChronoUnit.SECONDS), expiredAtAccess);
    LocalDateTime expiredAtRefresh = this.tokenReader.getExpiredAt(this.validRefreshToken);
    assertEquals(this.expiredAtRefreshToken.truncatedTo(ChronoUnit.SECONDS), expiredAtRefresh);
  }

  private String createToken(Instant expiredAt) {
    return JWT.create()
        .withIssuer(ISSUER)
        .withSubject(SUBJECT)
        .withIssuedAt(this.getInstant(0))
        .withExpiresAt(expiredAt)
        .sign(this.getAlgorithm());
  }

  private Instant getInstant(long plusSeconds) {
    return LocalDateTime.now().plusSeconds(plusSeconds).toInstant(ZoneOffset.UTC);
  }

  private Algorithm getAlgorithm() {
    return Algorithm.HMAC256(SECRET);
  }
}
