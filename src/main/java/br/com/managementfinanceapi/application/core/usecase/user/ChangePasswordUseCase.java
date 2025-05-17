package br.com.managementfinanceapi.application.core.usecase.user;

import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.application.port.in.user.ChangePasswordPort;
import br.com.managementfinanceapi.application.port.in.user.SearchUserPort;
import br.com.managementfinanceapi.application.port.out.user.SaveUserPort;
import org.springframework.stereotype.Service;

@Service
public class ChangePasswordUseCase implements ChangePasswordPort {

  private final SearchUserPort searchUserPort;
  private final SaveUserPort saveUserPort;

  public ChangePasswordUseCase(SearchUserPort searchUserPort, SaveUserPort saveUserPort) {
    this.searchUserPort = searchUserPort;
    this.saveUserPort = saveUserPort;
  }

  @Override
  public void change(String email, String password) {
    UserDomain userDomain = this.searchUserPort.byEmail(email);
    userDomain.changePassword(password);
    this.saveUserPort.execute(userDomain);
  }
}