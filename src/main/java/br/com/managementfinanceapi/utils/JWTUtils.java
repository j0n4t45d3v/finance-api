package br.com.managementfinanceapi.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JWTUtils {

  @Value("${security.jwt.secret}")
  private String secret;

  @Value("${security.jwt.issuer}")
  private String issuer;

  @Value("${security.jwt.expire-time}")
  private long expiredIn;

  public String generateToken(UserDetails user) {
    return JWT.create()
        .withIssuer(this.issuer)
        .withSubject(user.getUsername())
        .withExpiresAt(this.getExpiredAt())
        .sign(this.signKey());
  }

  private Instant getExpiredAt() {
    return LocalDateTime.now()
    .plusSeconds(this.expiredIn)
    .toInstant(ZoneOffset.UTC);
  }

  private Algorithm signKey() {
    return Algorithm.HMAC256(this.secret);
  }

  public boolean tokenIsValid(String token) {
    try {
      DecodedJWT jwtDecoded = JWT.decode(token);
      return !this.tokenIsExpired(jwtDecoded) && this.getIssuerIsValid(jwtDecoded);
    } catch (JWTDecodeException e) {
      return false;
    }
  }

  private boolean getIssuerIsValid(DecodedJWT jwtDecoded) {
    return jwtDecoded.getIssuer().equals(this.issuer);
  }


  private boolean tokenIsExpired(DecodedJWT jwtDecoded) {
    Instant instant = Instant.now();
    return jwtDecoded.getExpiresAtAsInstant().isAfter(instant);
  }

  public String getSubject(String token) {
    try {
      DecodedJWT jwtDecoded = JWT.decode(token);
      return jwtDecoded.getSubject();
    } catch (JWTDecodeException e) {
      return "";
    }
  }

  public long getExpireAt(String token) {
    DecodedJWT jwtDecoded = JWT.decode(token);
    return jwtDecoded.getExpiresAt().getTime();
  }

}