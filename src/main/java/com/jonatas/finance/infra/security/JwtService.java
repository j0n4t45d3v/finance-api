package com.jonatas.finance.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.SecretKey;

import com.jonatas.finance.domain.dvo.user.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.jonatas.finance.dto.Token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public record JwtService(
        @Value("${security.jwt.issuer}") String issuer,
        @Value("${security.jwt.access.secret}") String accessSecret,
        @Value("${security.jwt.access.exp}") Long accessExpiration,
        @Value("${security.jwt.refresh.secret}") String refreshSecret,
        @Value("${security.jwt.refresh.exp}") Long refreshExpiration
) {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    public record TokenParsed(Claims claims, String type) {

        public boolean isValid() {
            try {
                boolean expired = this.isExpired();
                boolean checkType = this.getType().equals(this.type);
                return !expired && checkType;
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
        return this.buildToken(subject, "access", this.accessExpiration, this.accessSecret);
    }

    public Token generateRefreshToken(UserDetails subject) {
        return this.buildToken(subject, "refresh", this.refreshExpiration, this.refreshSecret);
    }

    private Token buildToken(
            UserDetails subject,
            String type,
            Long expiration,
            String secret) {
        Date exp = new Date(System.currentTimeMillis() + expiration);
        String token = Jwts.builder()
                .id(UUID.randomUUID().toString())
                .issuer(this.issuer)
                .issuedAt(new Date())
                .subject(subject.getUsername())
                .claim("type", type)
                .expiration(exp)
                .signWith(this.getSecretKey(secret))
                .compact();
        return new Token(token, LocalDateTime.ofInstant(exp.toInstant(), ZoneId.of("UTC")));
    }

    public Optional<TokenParsed> tryParseAccessToken(String token){
        return  this.tryParseToken(token, "access", this.accessSecret);
    }

    public Optional<TokenParsed> tryParseRefreshToken(String token){
        return  this.tryParseToken(token, "refresh", this.refreshSecret);
    }

    public Optional<TokenParsed> tryParseToken(String token, String type, String secret){
        try{
            return Optional.of(new TokenParsed(this.getClaims(token, secret), type));
        }catch (Exception e) {
            return Optional.empty();
        }
    }

    public boolean isValidAccessToken(String token) {
        return this.isValidToken(token, this.accessSecret, "access");
    }

    public boolean isValidRefreshToken(String token) {
        return this.isValidToken(token, this.refreshSecret, "refresh");
    }

    private boolean isValidToken(String token, String secret, String type) {
        try {
            boolean expired = this.isExpired(token, secret);
            boolean checkType = this.getType(token, secret).equals(type);
            return !expired && checkType;
        } catch (Exception error) {
            log.error("Fail validate token {}", error);
            return false;
        }
    }

    public boolean isExpired(String token, String secret) {
        Instant expiration = this.getExpiration(token, secret).toInstant();
        Instant now = new Date(System.currentTimeMillis()).toInstant();
        return expiration.isBefore(now);
    }

    public String getSubjectAccessToken(String token) {
        return this.getClaims(token, this.accessSecret())
                .getSubject();
    }

    public Date getExpiration(String token, String secret) {
        return this.getClaims(token, secret)
                .getExpiration();
    }

    public String getType(String token, String secret) {
        return this
                .getClaims(token, secret)
                .get("type", String.class);
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
