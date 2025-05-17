package br.com.managementfinanceapi.application.core.usecase.user;

import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.application.port.in.user.LoginPort;
import br.com.managementfinanceapi.application.port.in.user.SearchUserPort;
import br.com.managementfinanceapi.infra.error.exceptions.user.InvalidCredentials;
import br.com.managementfinanceapi.infra.error.exceptions.user.UserNotFound;

public class LoginUseCase implements LoginPort {

  private final SearchUserPort searchUserPort;

  public LoginUseCase(SearchUserPort searchUserPort) {
    this.searchUserPort = searchUserPort;
  }

  @Override
  public UserDomain execute(UserDomain login) {
    try {
      UserDomain userFound = this.searchUserPort.byEmail(login.getEmail());
      if (userFound.notMatchPassword(login.getPassword())) {
        throw new InvalidCredentials("Usuário ou senha inválida");
      }
      return userFound;
    } catch (UserNotFound e) {
      throw new InvalidCredentials("Usuário ou senha inválida");
    }
  }

}