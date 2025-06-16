package br.com.managementfinanceapi.adapter.out.security.jwt;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Payload;

import br.com.managementfinanceapi.application.port.out.security.jwt.TokenReaderPort;

@Component
public class TokenReader implements TokenReaderPort{
  
  @Value("${security.jwt.secret}")
  private String secret;

  public String getSubject(String token) {
    return this.getPayload(token).getSubject();
  }

  @Override
  public boolean isExpired(String token) {
    return this.getExpiredAt(token).isAfter(LocalDateTime.now(ZoneOffset.UTC.normalized()));
  }

  @Override
  public LocalDateTime getExpiredAt(String token) {
    Instant expiredAt = this.getPayload(token)
                            .getExpiresAtAsInstant();
    return LocalDateTime.ofInstant(expiredAt, ZoneOffset.UTC.normalized());
  }

  @Override
  public Payload getPayload(String token) {
    return JWT.decode(token);
  }


}