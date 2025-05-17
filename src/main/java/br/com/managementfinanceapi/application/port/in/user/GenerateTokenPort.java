package br.com.managementfinanceapi.application.port.in.user;

import br.com.managementfinanceapi.adapter.in.dto.auth.Token;
import br.com.managementfinanceapi.adapter.in.dto.auth.TokenResponse;
import br.com.managementfinanceapi.application.core.domain.user.UserDomain;


public interface GenerateTokenPort {
  TokenResponse all(UserDomain userDetails);
  TokenResponse refresh(String refreshToken);
  Token accessToken(UserDomain userDetails);
  Token refreshToken(UserDomain userDetails);
}