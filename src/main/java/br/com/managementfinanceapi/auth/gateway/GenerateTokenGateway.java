package br.com.managementfinanceapi.auth.gateway;

import br.com.managementfinanceapi.auth.domain.dto.Token;
import br.com.managementfinanceapi.auth.domain.dto.TokenResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface GenerateTokenGateway {
  TokenResponse all(UserDetails userDetails);
  TokenResponse refresh(String refreshToken);
  Token accessToken(UserDetails userDetails);
  Token refreshToken(UserDetails userDetails);
}
