package com.jonatas.finance.infra.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import com.jayway.jsonpath.JsonPath;
import com.jonatas.finance.auth.Email;
import com.jonatas.finance.auth.Password;
import com.jonatas.finance.auth.User;
import com.jonatas.finance.helper.JWTHelper;
import io.jsonwebtoken.Claims;

class JwtServiceTest {

  private static final String TOKEN_ISSUER = "fake@issuer.test";
  private static final Long TOKEN_ACCESS_EXPIRATION = 3600000L;
  private static final String TOKEN_ACCESS_SECRET = "ZmFrZUFjY2Vzc1NlY3JldGZha2VBY2Nlc3NTZWNyZXRmYWtlQWNjZXNzU2VjcmV0" ;
  private static final Long TOKEN_REFRESH_EXPIRATION = 86400000L;
  private static final String TOKEN_REFRESH_SECRET = "ZmFrZVJlZnJlc2hTZWNyZXRmYWtlUmVmcmVzaFNlY3JldGZha2VSZWZyZXNoU2VjcmV0";

  private JwtService jwtService;

  @BeforeEach
  void setUp() {
    jwtService = new JwtService(
      TOKEN_ISSUER,
      TOKEN_ACCESS_SECRET,
      TOKEN_ACCESS_EXPIRATION,
      TOKEN_REFRESH_SECRET,
      TOKEN_REFRESH_EXPIRATION
    );
  }

  private User mockSubject() {
    return new User(
      new Email("testuser@mock.test"),
      new Password("password")
    );
  }

  @Nested
  class GenerateToken {

    @Test
    void shouldGenerateAccessToken() {
      var subject = mockSubject();
      var token = jwtService.generateToken(subject);

      JWTHelper.assertThat()
        .withIssuer(TOKEN_ISSUER)
        .withSubject(subject.getUsername())
        .withType("access")
        .withExpirationTime(TOKEN_ACCESS_EXPIRATION)
        .validate(token.value(), payload -> {
          assertEquals(token.expiredAt(), JsonPath.<Number>read(payload, "$.exp").longValue());
        });
    }

    @Test
    void shouldGenerateRefreshToken() {
      var subject = mockSubject();
      var token = jwtService.generateRefreshToken(subject);

      JWTHelper.assertThat().withIssuer(TOKEN_ISSUER).withSubject(subject.getUsername())
          .withType("refresh").withExpirationTime(TOKEN_REFRESH_EXPIRATION)
          .validate(token.value(), payload -> {
            assertEquals(token.expiredAt(), JsonPath.<Number>read(payload, "$.exp").longValue());
          });
    }

  }

  @Nested
  class ParseToken {

    @Test
    void shouldParseAccessToken() {
      var subject = mockSubject();
      var token = jwtService.generateToken(subject);
      var parsedSubject = jwtService.tryParseAccessToken(token.value());

      var tokenParsed = parsedSubject.orElseThrow();
      assertTrue(tokenParsed.isValid());
      assertEquals(subject.getUsername(), tokenParsed.getSubject().value());
      assertEquals("access", tokenParsed.getType());
    }

    @Test
    void shouldParseRefreshToken() {
      var subject = mockSubject();
      var token = jwtService.generateRefreshToken(subject);
      var parsedSubject = jwtService.tryParseRefreshToken(token.value());

      var tokenParsed = parsedSubject.orElseThrow();
      assertEquals(subject.getUsername(), tokenParsed.getSubject().value());
      assertEquals("refresh", tokenParsed.getType());
    }

    @Test
    void shouldNotParseExpiredToken() {
      var expiredToken = generateTokenExpired();
      var parsedSubject = jwtService.tryParseAccessToken(expiredToken);
      assertTrue(parsedSubject.isEmpty());
    }

    @Test
    void shouldNotParseInvalidToken() {
      var parsedSubject = jwtService.tryParseAccessToken("invalid");
      assertTrue(parsedSubject.isEmpty());
    }

    @Test
    void shouldNotParseWhenSignatureIsInvalid() {
      var subject = mockSubject();
      var token = jwtService.generateToken(subject);

      var invalidToken = token.value() + "invalid";
      var parsedSubject = jwtService.tryParseAccessToken(invalidToken);
      assertTrue(parsedSubject.isEmpty());
    }

    private String generateTokenExpired() {
      var subject = mockSubject();
      var jwtServiceWithCustomExpiration = this.buildJwtServiceExpired();
      return jwtServiceWithCustomExpiration.generateToken(subject).value();
    }

    private JwtService buildJwtServiceExpired() {
      return new JwtService(
          TOKEN_ISSUER,
          TOKEN_ACCESS_SECRET,
          -1L,
          TOKEN_REFRESH_SECRET,
          TOKEN_REFRESH_EXPIRATION
      );
    }


  }

  @Nested
  class TokenParsedTest {

     class MockClaimsBuilder {
        private Claims mockClaims = Mockito.mock(Claims.class);

        private String subject = "subject@test.mock";
        private String type = "access";
        private Date expiration = new Date(System.currentTimeMillis() + 3600000);

        public MockClaimsBuilder withSubject(String subject) {
          this.subject = subject;
          return this;
        }

        public MockClaimsBuilder withType(String type) {
          this.type = type;
          return this;
        }

        public MockClaimsBuilder withExpiration(Date expiration) {
          this.expiration = expiration;
          return this;
        }

        public Claims build() {
          when(mockClaims.getSubject()).thenReturn(subject);
          when(mockClaims.get("type", String.class)).thenReturn(type);
          when(mockClaims.getExpiration()).thenReturn(expiration);
          return mockClaims;
        }
     }

     @Test
     void shouldBeValidWhenTokenIsValid() {
       var mockClaims = new MockClaimsBuilder().build();
       var parsedToken = new JwtService.TokenParsed(mockClaims, "access");
       assertTrue(parsedToken.isValid());
       assertFalse(parsedToken.isExpired());
     }

    @Test
    void shouldNotBeValidWhenTokenIsExpired() {
      var mockClaims = new MockClaimsBuilder()
          .withExpiration(new Date(System.currentTimeMillis() - 1000))
          .build();
      var parsedToken = new JwtService.TokenParsed(mockClaims, "access");
      assertFalse(parsedToken.isValid());
      assertTrue(parsedToken.isExpired());
    }

    @Test
    void shouldNotBeValidWhenTypeIsInvalid() {
      var mockClaims = new MockClaimsBuilder()
          .withType("invalid")
          .build();
      var parsedToken = new JwtService.TokenParsed(mockClaims, "access");
      assertFalse(parsedToken.isValid());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", " "})
    void shouldNotBeValidWithoutSubject(String subject) {
      var mockClaims = new MockClaimsBuilder()
          .withSubject(subject)
          .build();
      var parsedToken = new JwtService.TokenParsed(mockClaims, "access");
      assertFalse(parsedToken.isValid());
    }
  }

}
