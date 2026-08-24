package com.jonatas.finance.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.jayway.jsonpath.JsonPath;
import java.util.Base64;
import java.util.function.Consumer;
import org.assertj.core.api.Assertions;

public class JWTHelper {

  private String issuer;
  private Long expirationTime;
  private String type;
  private String subject;

  public static JWTHelper assertThat() {
    return new JWTHelper();
  }

  public JWTHelper withIssuer(String issuer) {
    this.issuer = issuer;
    return this;
  }

  public JWTHelper withExpirationTime(Long expirationTime) {
    this.expirationTime = expirationTime;
    return this;
  }

  public JWTHelper withType(String type) {
    this.type = type;
    return this;
  }

  public JWTHelper withSubject(String subject) {
    this.subject = subject;
    return this;
  }

  public void validate(String token) {
    validate(token, payload -> {});
  }

  public void validate(String token, Consumer<String> additionalValidations) {
    assertNotNull(token);
    var decodedToken = decodeTokenJWT(token);

    assertEquals(this.subject, JsonPath.<String>read(decodedToken, "$.sub"));
    assertEquals(this.type, JsonPath.<String>read(decodedToken, "$.type"));
    Assertions.assertThat(getExpirationTime(decodedToken))
        .isBetween(this.expirationTime - 1, this.expirationTime + 1);
    assertEquals(this.issuer, JsonPath.<String>read(decodedToken, "$.iss"));

    if (additionalValidations != null) {
      additionalValidations.accept(decodedToken);
    }
  }

  private String decodeTokenJWT(String token) {
    var parts = token.split("\\.");
    var payload = parts[1];
    var payloadBytes = Base64.getUrlDecoder().decode(payload);
    return new String(payloadBytes);
  }

  private Long getExpirationTime(String payload) {
    var issueAt = JsonPath.<Number>read(payload, "$.iat").longValue();
    var expiredAt = JsonPath.<Number>read(payload, "$.exp").longValue();
    return expiredAt - issueAt;
  }
}
