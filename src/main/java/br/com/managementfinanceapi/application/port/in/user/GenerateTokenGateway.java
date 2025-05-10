package br.com.managementfinanceapi.application.port.in.user;

import br.com.managementfinanceapi.application.core.domain.user.dto.auth.Token;
import br.com.managementfinanceapi.application.core.domain.user.dto.auth.TokenResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface GenerateTokenGateway {
  TokenResponse all(UserDetails userDetails);
  TokenResponse refresh(String refreshToken);
  Token accessToken(UserDetails userDetails);
  Token refreshToken(UserDetails userDetails);
}
