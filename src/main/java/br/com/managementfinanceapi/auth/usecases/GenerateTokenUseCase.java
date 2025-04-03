package br.com.managementfinanceapi.auth.usecases;

import br.com.managementfinanceapi.auth.domain.dto.Token;
import br.com.managementfinanceapi.auth.domain.dto.TokenResponse;
import br.com.managementfinanceapi.auth.exceptions.InvalidTokenException;
import br.com.managementfinanceapi.auth.gateway.GenerateTokenGateway;
import br.com.managementfinanceapi.user.gateways.FindOneUser;
import br.com.managementfinanceapi.utils.JWTUtils;
import com.auth0.jwt.interfaces.Claim;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class GenerateTokenUseCase implements GenerateTokenGateway {

  private final JWTUtils jwtUtils;
  private final FindOneUser findOneUser;

  public GenerateTokenUseCase(
      JWTUtils jwtUtils,
      FindOneUser findOneUser
  ) {
    this.jwtUtils = jwtUtils;
    this.findOneUser = findOneUser;
  }

  @Override
  public TokenResponse refresh(String refreshToken) {
    if (!this.jwtUtils.tokenIsValid(refreshToken)) {
      throw new InvalidTokenException();
    }
    String tokenType = this.jwtUtils
        .getClaim(refreshToken, "token_type")
        .map(Claim::asString)
        .orElseThrow(InvalidTokenException::new);

    if(!Objects.equals(tokenType, "REFRESH")) {
      throw new InvalidTokenException();
    }

    String subject = this.jwtUtils.getSubject(refreshToken);
    UserDetails user = this.findOneUser.byEmail(subject);
    return this.all(user);
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
    return new Token(tokenGenerated, this.jwtUtils.getExpireAt(tokenGenerated));
  }

  @Override
  public Token refreshToken(UserDetails userDetails) {
    var refreshTokenGenerated = this.jwtUtils.generateRefreshToken(userDetails);
    return new Token(refreshTokenGenerated, this.jwtUtils.getExpireAt(refreshTokenGenerated));
  }
}
