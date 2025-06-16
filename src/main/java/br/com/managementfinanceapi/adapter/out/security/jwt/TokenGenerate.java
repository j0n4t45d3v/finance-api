package br.com.managementfinanceapi.adapter.out.security.jwt;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.managementfinanceapi.application.port.out.security.jwt.GenerateTokenPort;


@Component
public class TokenGenerate implements GenerateTokenPort {

  @Value("${security.jwt.issuer}")
  private String issuer;
  @Value("${security.jwt.secret}")
  private String secret;
  @Value("${security.jwt.expire-time}")
  private long expireTime;
  @Value("${security.jwt-refresh.expire-time}")
  private long expireTimeRefreshToken;

  @Override
  public String access(String subject) {
    return this.createToken(subject, this.expireTime);
  }

  @Override
  public String refresh(String subject) {
    return this.createToken(subject, this.expireTimeRefreshToken);
  }

  private String createToken(String subject, long expireTime) {
    return JWT.create()
        .withIssuer(issuer)
        .withSubject(subject)
        .withIssuedAt(this.getInstant(0))
        .withExpiresAt(this.getInstant(expireTime))
        .sign(this.getAlgorithm());
  }

  private Instant getInstant(long plusSeconds) {
    return LocalDateTime.now()
        .plusSeconds(plusSeconds)
        .toInstant(ZoneOffset.UTC);
  }

  private Algorithm getAlgorithm() {
    return Algorithm.HMAC256(this.secret);
  }

}