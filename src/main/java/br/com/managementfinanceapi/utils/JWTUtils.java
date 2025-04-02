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
  private long accessExpiredTime;

  @Value("${security.jwt-refresh.expire-time}")
  private long refreshExpiredTime;

  public String generateAccessToken(UserDetails user) {
    return this.generateToken(user, this.getExpiredAt(this.accessExpiredTime));
  }

  public String generateRefreshToken(UserDetails user) {
    return this.generateToken(user, this.getExpiredAt(this.refreshExpiredTime));
  }

  public String generateToken(UserDetails user, Instant expiredAt) {
    return JWT.create()
        .withIssuer(this.issuer)
        .withSubject(user.getUsername())
        .withExpiresAt(expiredAt)
        .sign(this.signKey());
  }

  private Instant getExpiredAt(long plusSeconds) {
    return LocalDateTime.now()
    .plusSeconds(plusSeconds)
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