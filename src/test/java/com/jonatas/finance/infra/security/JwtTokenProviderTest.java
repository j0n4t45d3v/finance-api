package com.jonatas.finance.infra.security;

import static org.assertj.core.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import com.jonatas.finance.adapter.security.Token;
import com.jonatas.finance.faker.Faker;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

class JwtTokenProviderTest {

    private static final String TOKEN_ISSUER = "fake@issuer.test";
    private static final Long TOKEN_ACCESS_EXPIRATION = 3600000L;
    private static final String TOKEN_ACCESS_SECRET = "ZmFrZUFjY2Vzc1NlY3JldGZha2VBY2Nlc3NTZWNyZXRmYWtlQWNjZXNzU2VjcmV0";
    private static final Long TOKEN_REFRESH_EXPIRATION = 86400000L;
    private static final String TOKEN_REFRESH_SECRET = "ZmFrZVJlZnJlc2hTZWNyZXRmYWtlUmVmcmVzaFNlY3JldGZha2VSZWZyZXNoU2VjcmV0";

    private static final JwtConfig JWT_CONFIG = new JwtConfig(TOKEN_ISSUER,
                                                              tokenSignature(TOKEN_ACCESS_SECRET, TOKEN_ACCESS_EXPIRATION),
                                                              tokenSignature(TOKEN_REFRESH_SECRET, TOKEN_REFRESH_EXPIRATION));
    private JwtTokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        tokenProvider = new JwtTokenProvider(JWT_CONFIG);
    }

    @Nested
    class GeneratePairToken {
        @Test
        void shouldGeneratePairToken() {
            var user = Faker.user().get();
            var pairToken = tokenProvider.generatePairToken(user);
            assertThat(pairToken).isNotNull()
                                 .extracting("access", "refresh")
                                 .doesNotContainNull();

            assertToken(pairToken.access(), TOKEN_ACCESS_EXPIRATION, "access");
            assertToken(pairToken.refresh(), TOKEN_REFRESH_EXPIRATION, "refresh");
        }

        private void assertToken(Token token, long expirationTime, String expectedType) {
            var expectedExpiration = Instant.now()
                                            .plus(expirationTime,
                                                  ChronoUnit.MILLIS);

            String payload = token.value().split("\\.")[1];
            String payloadJson = new String(Base64.getDecoder().decode(payload), StandardCharsets.UTF_8);
            assertThat(token).isNotNull();
            assertThat(token.value()).isNotBlank();
            assertThat(token.expiration()).isNotNull()
                                          .isAfterOrEqualTo(expectedExpiration);
            String typeClaim = JsonPath.read(payloadJson, "$.type");
            assertThat(typeClaim).isNotBlank()
                                 .isEqualTo(expectedType);
        }

    }

    @Nested
    class ValidateAccessToken {
        @Test
        void shouldValidateAccessTokenAndReturnDecodedTokenWhenSuccess() {
            var user = Faker.user().get();
            var pairToken = tokenProvider.generatePairToken(user);
            var decodedToken = tokenProvider.validateAccessToken(pairToken.access().value());
            assertThat(decodedToken).isNotNull();
            assertThat(decodedToken.subject()).isEqualTo(user.getUsername());
            assertThat(decodedToken.type()).isNotBlank()
                                           .isEqualTo("access");
            assertThat(decodedToken.expiredAt()).isInTheFuture();
        }

        @ParameterizedTest
        @NullAndEmptySource
        void shouldThrowIllegalArgumentExceptionWhenTokenIsNullBlankOrEmpty(String token) {
            assertThatIllegalArgumentException().isThrownBy(() -> tokenProvider.validateAccessToken(token));
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("provideInvalidAccessTokens")
        void shouldThrowJwtExceptionWhenTokenIsInvalid(String scenery,
                                                       String token,
                                                       Class<? extends JwtException> expectedException) {
            assertThatException().isThrownBy(() -> tokenProvider.validateAccessToken(token))
                                 .isInstanceOf(expectedException);
        }

        static Stream<Arguments> provideInvalidAccessTokens() {
            var validToken = buildAccessToken(TOKEN_ISSUER,
                                              Faker.email(),
                                              TOKEN_ACCESS_EXPIRATION,
                                              TOKEN_ACCESS_SECRET);
            var refreshToken = buildRefreshToken(TOKEN_ISSUER,
                                                 Faker.email(),
                                                 TOKEN_REFRESH_EXPIRATION,
                                                 TOKEN_REFRESH_SECRET);
            var refreshTokenSignedWithAccessSecret = buildRefreshToken(TOKEN_ISSUER,
                                                                       Faker.email(),
                                                                       TOKEN_REFRESH_EXPIRATION,
                                                                       TOKEN_ACCESS_SECRET);

            var tokenFromAnotherIssuer = buildAccessToken("other-issuer",
                                                          Faker.email(),
                                                          TOKEN_ACCESS_EXPIRATION,
                                                          TOKEN_ACCESS_SECRET);

            var expiredToken = buildAccessToken(TOKEN_ISSUER,
                                                Faker.email(),
                                                -1,
                                                TOKEN_ACCESS_SECRET);
            var tokenWithInvalidSignature = buildAccessToken(TOKEN_ISSUER,
                                                             Faker.email(),
                                                             TOKEN_ACCESS_EXPIRATION,
                                                             TOKEN_REFRESH_SECRET);
            return Stream.of(Arguments.of("malformed token", "invalid-token", MalformedJwtException.class),
                             Arguments.of("malformed token", "another-invalid-token", MalformedJwtException.class),
                             Arguments.of("truncated token", validToken.substring(0, validToken.length() / 2), MalformedJwtException.class),

                             Arguments.of("token from another issuer", tokenFromAnotherIssuer, IncorrectClaimException.class),
                             Arguments.of("refresh token signed with access secret", refreshTokenSignedWithAccessSecret, IncorrectClaimException.class),

                             Arguments.of("expired token", expiredToken, ExpiredJwtException.class),

                             Arguments.of("refresh token", refreshToken, SignatureException.class),
                             Arguments.of("token with invalid signature", tokenWithInvalidSignature, SignatureException.class));
        }
    }

    @Nested
    class ValidateRefreshToken {

        @Test
        void shouldValidateRefreshTokenAndReturnDecodedTokenWhenSuccess() {
            var user = Faker.user().get();
            var pairToken = tokenProvider.generatePairToken(user);
            var decodedToken = tokenProvider.validateRefreshToken(pairToken.refresh().value());
            assertThat(decodedToken).isNotNull();
            assertThat(decodedToken.subject()).isNotNull()
                                              .isEqualTo(user.getUsername());
            assertThat(decodedToken.type()).isNotBlank()
                                           .isEqualTo("refresh");
            assertThat(decodedToken.expiredAt()).isNotNull()
                                                .isInTheFuture();
        }

        @ParameterizedTest
        @NullAndEmptySource
        void shouldThrowIllegalArgumentExceptionWhenTokenIsNullBlankOrEmpty(String token) {
            assertThatIllegalArgumentException().isThrownBy(() -> tokenProvider.validateRefreshToken(token));
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("provideInvalidRefreshTokens")
        void shouldThrowJwtExceptionWhenTokenIsInvalid(String scenery, String token, Class<? extends Throwable> expectedException) {
            assertThatException().isThrownBy(() -> tokenProvider.validateRefreshToken(token))
                                 .isInstanceOf(expectedException);
        }

        static Stream<Arguments> provideInvalidRefreshTokens() {
            var validToken = buildRefreshToken(TOKEN_ISSUER,
                                               Faker.email(),
                                               TOKEN_REFRESH_EXPIRATION,
                                               TOKEN_REFRESH_SECRET);
            var accessToken = buildAccessToken(TOKEN_ISSUER,
                                               Faker.email(),
                                               TOKEN_ACCESS_EXPIRATION,
                                               TOKEN_ACCESS_SECRET);
            var accessTokenSignedWithRefreshSecret = buildAccessToken(TOKEN_ISSUER,
                                                                      Faker.email(),
                                                                      TOKEN_ACCESS_EXPIRATION,
                                                                      TOKEN_REFRESH_SECRET);

            var tokenFromAnotherIssuer = buildRefreshToken("other-issuer",
                                                           Faker.email(),
                                                           TOKEN_REFRESH_EXPIRATION,
                                                           TOKEN_REFRESH_SECRET);

            var expiredToken = buildRefreshToken(TOKEN_ISSUER,
                                                 Faker.email(),
                                                 -1,
                                                 TOKEN_REFRESH_SECRET);
            var tokenWithInvalidSignature = buildRefreshToken(TOKEN_ISSUER,
                                                              Faker.email(),
                                                              TOKEN_REFRESH_EXPIRATION,
                                                              TOKEN_ACCESS_SECRET);
            return Stream.of(Arguments.of("malformed token", "invalid-token", MalformedJwtException.class),
                             Arguments.of("malformed token", "another-invalid-token", MalformedJwtException.class),
                             Arguments.of("truncated token", validToken.substring(0, validToken.length() / 2), MalformedJwtException.class),

                             Arguments.of("token from another issuer", tokenFromAnotherIssuer, IncorrectClaimException.class),
                             Arguments.of("access token signed with refresh secret", accessTokenSignedWithRefreshSecret, IncorrectClaimException.class),

                             Arguments.of("expired token", expiredToken, ExpiredJwtException.class),

                             Arguments.of("access token", accessToken, SignatureException.class),
                             Arguments.of("token with invalid signature", tokenWithInvalidSignature, SignatureException.class));
        }
    }

    private static String buildAccessToken(String issuer,
                                           String subject,
                                           long expiration,
                                           String secret) {
        return buildJwtToken(issuer,
                             subject,
                             Map.of("type", "access"),
                             expiration,
                             secret);
    }

    private static String buildRefreshToken(String issuer,
                                            String subject,
                                            long expiration,
                                            String secret) {
        return buildJwtToken(issuer,
                             subject,
                             Map.of("type", "refresh"),
                             expiration,
                             secret);
    }

    private static String buildJwtToken(String issuer,
                                        String subject,
                                        Map<String, String> claims,
                                        long expiration,
                                        String secret) {
        return Jwts.builder()
                   .id(UUID.randomUUID().toString())
                   .issuer(issuer)
                   .issuedAt(new Date())
                   .subject(subject)
                   .claims(claims)
                   .expiration(new Date(Instant.now()
                                               .plus(expiration, ChronoUnit.MILLIS)
                                               .toEpochMilli()))
                   .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)))
                   .compact();
    }

    private static JwtConfig.TokenSignatureConfig tokenSignature(String secret, long expirationTime) {
        return new JwtConfig.TokenSignatureConfig(secret, expirationTime);
    }

}