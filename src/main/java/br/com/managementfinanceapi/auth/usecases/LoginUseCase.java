package br.com.managementfinanceapi.auth.usecases;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import br.com.managementfinanceapi.auth.controller.AuthControllerV1.Token;
import br.com.managementfinanceapi.auth.domain.dto.Login;
import br.com.managementfinanceapi.auth.gateway.LoginGateway;
import br.com.managementfinanceapi.utils.JWTUtils;

@Service
public class LoginUseCase implements LoginGateway {

  private final UserDetailsService userDetailsService; 
  private final JWTUtils jwtUtils; 

  public LoginUseCase(UserDetailsService userDetailsService, JWTUtils jwtUtils) {
    this.userDetailsService = userDetailsService;
    this.jwtUtils = jwtUtils;
  }

  @Override
  public Token execute(Login login) {
    UserDetails user = this.userDetailsService.loadUserByUsername(login.email());
    String tokenGenerated = this.jwtUtils.generateToken(user);
    long expiredAt = this.jwtUtils.getExpireAt(tokenGenerated);
    return new Token(tokenGenerated, tokenGenerated, expiredAt);
  }

}