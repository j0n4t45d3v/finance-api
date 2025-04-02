package br.com.managementfinanceapi.auth.gateway;

import br.com.managementfinanceapi.auth.controller.AuthControllerV1.Token;
import br.com.managementfinanceapi.auth.controller.AuthControllerV1.TokenResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface GenerateTokenGateway {
  TokenResponse all(UserDetails userDetails);
  Token accessToken(UserDetails userDetails);
  Token refreshToken(UserDetails userDetails);
}
