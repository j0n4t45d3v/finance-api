package br.com.managementfinanceapi.application.core.usecase.user;

import br.com.managementfinanceapi.application.core.domain.user.User;
import br.com.managementfinanceapi.infra.error.exceptions.user.InvalidPassword;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.managementfinanceapi.application.core.domain.user.dto.auth.Login;
import br.com.managementfinanceapi.application.port.in.user.LoginGateway;

@Service
public class LoginUseCase implements LoginGateway {

  private final UserDetailsService userDetailsService; 

  public LoginUseCase(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @Override
  public UserDetails execute(Login login) {
    try {
      User userDetails = (User) this.userDetailsService
          .loadUserByUsername(login.email());

      if(userDetails.differentPassword(login.password())) {
        throw new InvalidPassword("Usuário ou senha inválida");
      }
      return userDetails;
    } catch (UsernameNotFoundException e) {
      throw new InvalidPassword("Usuário ou senha inválida");
    }
  }

}