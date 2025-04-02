package br.com.managementfinanceapi.auth.usecases;

import br.com.managementfinanceapi.auth.domain.dto.Token;
import br.com.managementfinanceapi.auth.domain.dto.TokenResponse;
import br.com.managementfinanceapi.auth.gateway.GenerateTokenGateway;
import br.com.managementfinanceapi.utils.JWTUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class GenerateTokenUseCase implements GenerateTokenGateway {

  private final JWTUtils jwtUtils;

  public GenerateTokenUseCase(JWTUtils jwtUtils) {
    this.jwtUtils = jwtUtils;
  }

  @Override
  public TokenResponse all(UserDetails userDetails) {
    Token accessToken = this.accessToken(userDetails);
    Token refreshToken = this.refreshToken(userDetails);
    return new TokenResponse(accessToken, refreshToken);
  }

  @Override
  public Token accessToken(UserDetails userDetails) {
    var tokenGenerated = this.jwtUtils.generateAccessToken(userDetails);
    return new Token(tokenGenerated,  this.jwtUtils.getExpireAt(tokenGenerated));
  }

  @Override
  public Token refreshToken(UserDetails userDetails) {
    var refreshTokenGenerated = this.jwtUtils.generateRefreshToken(userDetails);
    return new Token(refreshTokenGenerated,  this.jwtUtils.getExpireAt(refreshTokenGenerated));
  }
}
