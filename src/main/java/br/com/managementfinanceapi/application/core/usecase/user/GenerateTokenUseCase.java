package br.com.managementfinanceapi.application.core.usecase.user;

import br.com.managementfinanceapi.adapter.in.dto.auth.Token;
import br.com.managementfinanceapi.adapter.in.dto.auth.TokenResponse;
import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.application.port.in.user.GenerateTokenPort;

public class GenerateTokenUseCase implements GenerateTokenPort {

  // private final JWTPort jwtUtils;
  // private final SearchUserPort searchUserPort;
  //
  // public GenerateTokenUseCase(
  //     JWTUtils jwtUtils,
  //     SearchUserPort searchUserPort
  // ) {
  //   this.jwtUtils = jwtUtils;
  //   this.searchUserPort = searchUserPort;
  // }

  @Override
  public TokenResponse refresh(String refreshToken) {
//    if (!this.jwtUtils.tokenIsValid(refreshToken)) {
//      throw new InvalidTokenException();
//    }
//    String tokenType = this.jwtUtils
//        .getClaim(refreshToken, "token_type")
//        .map(Claim::asString)
//        .orElseThrow(InvalidTokenException::new);
//
//    if(!Objects.equals(tokenType, "REFRESH")) {
//      throw new InvalidTokenException();
//    }
//
//    String subject = this.jwtUtils.getSubject(refreshToken);
//    UserDetails user = this.searchUserPort.byEmail(subject);
//    return this.all(user);
    return null;
  }

  @Override
  public TokenResponse all(UserDomain userDetails) {
    Token accessToken = this.accessToken(userDetails);
    Token refreshToken = this.refreshToken(userDetails);
    return new TokenResponse(accessToken, refreshToken);
  }

  @Override
  public Token accessToken(UserDomain userDetails) {
    // var tokenGenerated = this.jwtUtils.generateAccessToken(userDetails);
    // return new Token(tokenGenerated, this.jwtUtils.getExpireAt(tokenGenerated));
    return null;
  }

  @Override
  public Token refreshToken(UserDomain userDetails) {
    // var refreshTokenGenerated = this.jwtUtils.generateRefreshToken(userDetails);
    // return new Token(refreshTokenGenerated, this.jwtUtils.getExpireAt(refreshTokenGenerated));
    return null;
  }
}