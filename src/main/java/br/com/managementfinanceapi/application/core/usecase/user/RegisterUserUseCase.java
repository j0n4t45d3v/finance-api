package br.com.managementfinanceapi.application.core.usecase.user;

import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.application.port.out.user.SaveUserPort;
import br.com.managementfinanceapi.application.port.out.user.FindUserPort;
import br.com.managementfinanceapi.infra.error.exceptions.user.EmailAlreadyUsed;
import br.com.managementfinanceapi.infra.error.exceptions.user.InvalidPassword;
import br.com.managementfinanceapi.application.port.in.user.RegisterUserPort;

public class RegisterUserUseCase implements RegisterUserPort {

  private final SaveUserPort saveUserPort;
  private final FindUserPort findUserPort;

  public RegisterUserUseCase(SaveUserPort saveUserPort, FindUserPort findUserPort) {
    this.saveUserPort = saveUserPort;
    this.findUserPort = findUserPort;
  }

  @Override
  public UserDomain execute(UserDomain body, String confirmPassword) {
    if (this.findUserPort.byEmail(body.getEmail()).isPresent()) {
      throw new EmailAlreadyUsed();
    }
    if(body.notMatchPassword(confirmPassword)) {
      throw new InvalidPassword("As senhas são divergentes");
    }
    return this.saveUserPort.execute(body);
  }
}
