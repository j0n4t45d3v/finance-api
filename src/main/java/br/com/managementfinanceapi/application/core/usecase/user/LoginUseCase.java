package br.com.managementfinanceapi.application.core.usecase.user;

import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.application.core.domain.user.exception.InvalidCredentials;
import br.com.managementfinanceapi.application.core.domain.user.exception.UserNotFound;
import br.com.managementfinanceapi.application.port.in.user.LoginPort;
import br.com.managementfinanceapi.application.port.in.user.SearchUserPort;
import br.com.managementfinanceapi.application.port.out.security.HashPasswordPort;

public class LoginUseCase implements LoginPort {

  private final SearchUserPort searchUserPort;
  private final HashPasswordPort hashPasswordPort;

  public LoginUseCase(
    SearchUserPort searchUserPort,
    HashPasswordPort hashPasswordPort
  ) {
    this.searchUserPort = searchUserPort;
    this.hashPasswordPort = hashPasswordPort;
  }

  @Override
  public UserDomain execute(UserDomain login) {
    try {
      UserDomain userFound = this.searchUserPort.byEmail(login.getEmail());
      if (!this.hashPasswordPort.matchers(userFound.getPassword(), login.getPassword())) {
        throw new InvalidCredentials("Usuário ou senha inválida");
      }
      return userFound;
    } catch (UserNotFound e) {
      throw new InvalidCredentials("Usuário ou senha inválida");
    }
  }

}