package br.com.managementfinanceapi.auth.usecases;

import br.com.managementfinanceapi.user.domain.User;
import br.com.managementfinanceapi.user.exceptions.InvalidPassword;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.managementfinanceapi.auth.domain.dto.Login;
import br.com.managementfinanceapi.auth.gateway.LoginGateway;

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