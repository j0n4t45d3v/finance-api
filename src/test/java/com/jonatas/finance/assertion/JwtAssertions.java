package com.jonatas.finance.assertion;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import com.jonatas.finance.adapter.security.TokenInfo;
import org.assertj.core.api.Assertions;

import com.jayway.jsonpath.JsonPath;

public class JwtAssertions {

    private final String header;
    private final String payload;

    private JwtAssertions(String header, String payload) {
        this.header = header;
        this.payload = payload;
    }

    public static JwtAssertions assertThat(TokenInfo token) {
        return assertThat(token.value());
    }

    public static JwtAssertions assertThat(String token) {
        var jwtSections = decodeTokenJWT(token);
        return new JwtAssertions(decodeBase64ToString(jwtSections[0]),
                                 decodeBase64ToString(jwtSections[1]));
    }

    private static String[] decodeTokenJWT(String token) {
        var parts = token.split("\\.");
        Assertions.assertThat(parts)
                  .hasSize(3);
        return parts;
    }

    private static String decodeBase64ToString(String content) {
        var data = Base64.getUrlDecoder().decode(content);
        return new String(data, StandardCharsets.UTF_8);
    }

    public JwtAssertions hasAlgorithm(String expectedAlgorithm) {
        String algorithm = this.<String>jsonPath(header, "$.alg")
                               .orElse("");
        Assertions.assertThat(algorithm)
                  .isNotBlank()
                  .isEqualTo(expectedAlgorithm);
        return this;
    }

    public JwtAssertions hasIssuer(String expectedIssuer) {
        String issuer = this.<String>jsonPath(payload, "$.iss")
                            .orElse("");
        Assertions.assertThat(issuer)
                  .isNotBlank()
                  .isEqualTo(expectedIssuer);
        return this;
    }

    public JwtAssertions hasSubject(String expectedSubject) {
        String subject = this.<String>jsonPath(payload, "$.sub")
                             .orElse("");
        Assertions.assertThat(subject)
                  .isNotBlank()
                  .isEqualTo(expectedSubject);
        return this;
    }

    public JwtAssertions hasExpiration(long expectedExpiration) {
        Assertions.assertThat(getExpirationTime(payload))
                  .isBetween(expectedExpiration - 1, expectedExpiration + 1);
        return this;
    }

    public JwtAssertions has(String claimKey, Object expected) {
        Object claim = jsonPath(payload, "$." + claimKey).orElse(null);
        Assertions.assertThat(claim)
                  .isNotNull()
                  .isEqualTo(expected);
        return this;
    }

    private Long getExpirationTime(String payload) {
        long issueAt = this.<Number>jsonPath(payload, "$.iat")
                           .map(Number::longValue)
                           .orElse(0L);
        long expiredAt = this.<Number>jsonPath(payload, "$.exp")
                             .map(Number::longValue)
                             .orElse(0L);
        return expiredAt - issueAt;
    }

    private <T> Optional<T> jsonPath(String json, String path) {
        if (!JsonPath.isPathDefinite(path)) {
            return Optional.empty();
        }
        return Optional.of(JsonPath.read(json, path));
    }

}