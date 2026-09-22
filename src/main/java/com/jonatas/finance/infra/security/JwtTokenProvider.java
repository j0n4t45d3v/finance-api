package com.jonatas.finance.infra.security;

import com.jonatas.finance.adapter.security.DecodedToken;
import com.jonatas.finance.adapter.security.PairToken;
import com.jonatas.finance.adapter.security.Token;
import com.jonatas.finance.adapter.security.TokenProvider;
import com.jonatas.finance.auth.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtTokenProvider implements TokenProvider {

    public static final String CLAIM_KEY_TYPE = "type";
    public static final String CLAIM_TYPE_ACCESS = "access";
    public static final String CLAIM_TYPE_REFRESH = "refresh";

    private final JwtConfig jwtConfig;

    public JwtTokenProvider(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    public PairToken generatePairToken(User user) {
        var accessToken = buildToken(user, Map.of(CLAIM_KEY_TYPE, CLAIM_TYPE_ACCESS), this.jwtConfig.access());
        var refreshToken = buildToken(user, Map.of(CLAIM_KEY_TYPE, CLAIM_TYPE_REFRESH), this.jwtConfig.refresh());
        return new PairToken(accessToken, refreshToken);
    }

    private Token buildToken(User subject,
                             Map<String, Object> claims,
                             JwtConfig.TokenSignatureConfig signature) {
        Instant exp = Instant.now().plusSeconds(signature.exp());
        String token = Jwts.builder()
                           .id(UUID.randomUUID().toString())
                           .issuer(this.jwtConfig.issuer())
                           .issuedAt(new Date())
                           .subject(subject.getUsername())
                           .claims(claims)
                           .expiration(new Date(exp.toEpochMilli()))
                           .signWith(secretKey(signature.secret()))
                           .compact();
        return new Token(token, exp);
    }

    @Override
    public DecodedToken validateAccessToken(String token) {
        return validateToken(token, CLAIM_TYPE_ACCESS, this.jwtConfig.access());
    }

    @Override
    public DecodedToken validateRefreshToken(String token) {
        return validateToken(token, CLAIM_TYPE_REFRESH, this.jwtConfig.refresh());
    }

    private DecodedToken validateToken(String token,
                                       String type,
                                       JwtConfig.TokenSignatureConfig signature) {
        var claims = getClaims(token, type, signature);
        return new DecodedToken(claims.getId(),
                                claims.getSubject(),
                                claims.get(CLAIM_KEY_TYPE, String.class),
                                claims.getExpiration().toInstant(),
                                claims.getIssuedAt().toInstant());
    }

    private Claims getClaims(String token,
                             String type,
                             JwtConfig.TokenSignatureConfig signature) {
        return Jwts.parser()
                   .verifyWith(secretKey(signature.secret()))
                   .requireIssuer(this.jwtConfig.issuer())
                   .require(CLAIM_KEY_TYPE, type)
                   .build()
                   .parseSignedClaims(token)
                   .getPayload();
    }

    private SecretKey secretKey(String secretRaw) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretRaw));
    }
}
