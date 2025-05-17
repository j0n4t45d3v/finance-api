package br.com.managementfinanceapi.application.port.in;

import com.auth0.jwt.interfaces.Claim;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface JWTPort {

  String generateAccessToken(UserDetails user);

  String generateRefreshToken(UserDetails user);

  boolean tokenIsValid(String token);

  String getSubject(String token);

  Optional<Claim> getClaim(String token, String claim);

  long getExpireAt(String token);
}