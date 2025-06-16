package br.com.managementfinanceapi.application.port.out.security.jwt;

import java.time.LocalDateTime;

import com.auth0.jwt.interfaces.Payload;

public interface TokenReaderPort {
  
  String getSubject(String token);
  Payload getPayload(String token);
  boolean isExpired(String token);
  LocalDateTime getExpiredAt(String token);

}