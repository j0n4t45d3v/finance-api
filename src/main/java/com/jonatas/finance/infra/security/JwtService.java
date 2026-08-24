package com.jonatas.finance.infra.security;

import com.jonatas.finance.auth.Email;
import com.jonatas.finance.common.dto.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public record JwtService(JwtConfig jwtConfig) {

  public record TokenParsed(Claims claims, String type) {

    public boolean isValid() {
      try {
        boolean expired = this.isExpired();
        boolean checkType = this.getType().equals(this.type);
        return !expired && checkType && this.getSubject() != null;
      } catch (Exception error) {
        return false;
      }
    }

    public boolean isExpired() {
      Instant expiration = this.getExpiration().toInstant();
      Instant now = new Date(System.currentTimeMillis()).toInstant();
      return expiration.isBefore(now);
    }

    public Email getSubject() {
      return new Email(this.claims.getSubject());
    }

    public Date getExpiration() {
      return this.claims.getExpiration();
    }

    public String getType() {
      return this.claims.get("type", String.class);
    }
  }

  public Token generateToken(UserDetails subject) {
    return this.buildToken(subject, "access", this.jwtConfig.access());
  }

  public Token generateRefreshToken(UserDetails subject) {
    return this.buildToken(subject, "refresh", this.jwtConfig.refresh());
  }

  private Token buildToken(
      UserDetails subject, String type, JwtConfig.TokenSignatureConfig tokenSignatureConfig) {

    Instant exp = Instant.now().plusSeconds(tokenSignatureConfig.exp());
    String token =
        Jwts.builder()
            .id(UUID.randomUUID().toString())
            .issuer(this.jwtConfig.issuer())
            .issuedAt(new Date())
            .subject(subject.getUsername())
            .claim("type", type)
            .expiration(new Date(exp.toEpochMilli()))
            .signWith(this.getSecretKey(tokenSignatureConfig.secret()))
            .compact();
    return new Token(token, exp.getEpochSecond());
  }

  public Optional<TokenParsed> tryParseAccessToken(String token) {
    return this.tryParseToken(token, "access", this.jwtConfig.accessSecret());
  }

  public Optional<TokenParsed> tryParseRefreshToken(String token) {
    return this.tryParseToken(token, "refresh", this.jwtConfig.refreshSecret());
  }

  public Optional<TokenParsed> tryParseToken(String token, String type, String secret) {
    try {
      return Optional.of(new TokenParsed(this.getClaims(token, secret), type));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  public Claims getClaims(String token, String secret) {
    return Jwts.parser()
        .verifyWith(this.getSecretKey(secret))
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  private SecretKey getSecretKey(String secretRaw) {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretRaw));
  }
}
