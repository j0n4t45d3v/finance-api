package br.com.managementfinanceapi.application.core.usecase.user;

import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.application.port.in.user.ChangePasswordPort;
import br.com.managementfinanceapi.application.port.in.user.SearchUserPort;
import br.com.managementfinanceapi.application.port.out.security.HashPasswordPort;
import br.com.managementfinanceapi.application.port.out.user.SaveUserPort;

public class ChangePasswordUseCase implements ChangePasswordPort {

  private final SearchUserPort searchUserPort;
  private final HashPasswordPort hashPasswordPort;
  private final SaveUserPort saveUserPort;

  public ChangePasswordUseCase(
      SearchUserPort searchUserPort,
      HashPasswordPort hashPasswordPort,
      SaveUserPort saveUserPort
  ) {
    this.searchUserPort = searchUserPort;
    this.hashPasswordPort = hashPasswordPort;
    this.saveUserPort = saveUserPort;
  }

  @Override
  public void change(String email, String password) {
    UserDomain userDomain = this.searchUserPort.byEmail(email);
    userDomain.changePassword(password);
    userDomain.encodePassword(this.hashPasswordPort);
    this.saveUserPort.execute(userDomain);
  }
}