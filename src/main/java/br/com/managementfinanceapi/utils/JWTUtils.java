package br.com.managementfinanceapi.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

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

  enum TypeToken {
    ACCESS, REFRESH
  }

  public String generateAccessToken(UserDetails user) {
    Instant expiredAt = this.getExpiredAt(this.accessExpiredTime);
    return this.generateToken(user, expiredAt, TypeToken.ACCESS);
  }

  public String generateRefreshToken(UserDetails user) {
    Instant expiredAt = this.getExpiredAt(this.refreshExpiredTime);
    return this.generateToken(user, expiredAt, TypeToken.REFRESH);
  }

  private String generateToken(UserDetails user, Instant expiredAt, TypeToken type) {
    return JWT.create()
        .withIssuer(this.issuer)
        .withSubject(user.getUsername())
        .withClaim("token_type",type.name())
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
    Instant now = LocalDateTime.now()
        .toInstant(ZoneOffset.UTC);

    Instant expiredAt = jwtDecoded.getExpiresAtAsInstant();
    return expiredAt.isBefore(now);
  }

  public String getSubject(String token) {
    try {
      DecodedJWT jwtDecoded = JWT.decode(token);
      return jwtDecoded.getSubject();
    } catch (JWTDecodeException e) {
      return "";
    }
  }

  public Optional<Claim> getClaim(String token, String claim) {
      DecodedJWT jwtDecoded = JWT.decode(token);
      return Optional.ofNullable(jwtDecoded.getClaim(claim));
  }


  public long getExpireAt(String token) {
    DecodedJWT jwtDecoded = JWT.decode(token);
    return jwtDecoded.getExpiresAt().getTime();
  }

}