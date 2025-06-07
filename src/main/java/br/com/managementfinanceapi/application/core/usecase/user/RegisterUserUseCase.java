package br.com.managementfinanceapi.application.core.usecase.user;

import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.application.port.out.user.SaveUserPort;
import br.com.managementfinanceapi.application.port.out.security.HashPasswordPort;
import br.com.managementfinanceapi.application.port.out.user.FindUserPort;
import br.com.managementfinanceapi.application.core.domain.user.exception.EmailAlreadyUsed;
import br.com.managementfinanceapi.application.core.domain.user.exception.InvalidPassword;
import br.com.managementfinanceapi.application.port.in.user.RegisterUserPort;

public class RegisterUserUseCase implements RegisterUserPort {

  private final SaveUserPort saveUserPort;
  private final FindUserPort findUserPort;
  private final HashPasswordPort hashPasswordPort;

  public RegisterUserUseCase(
    SaveUserPort saveUserPort,
    FindUserPort findUserPort,
    HashPasswordPort hashPasswordPort
  ) {
    this.saveUserPort = saveUserPort;
    this.findUserPort = findUserPort;
    this.hashPasswordPort = hashPasswordPort;
  }

  @Override
  public UserDomain execute(UserDomain body, String confirmPassword) {
    if (this.findUserPort.byEmail(body.getEmail()).isPresent()) {
      throw new EmailAlreadyUsed();
    }
    body.encodePassword(this.hashPasswordPort);
    if(hashPasswordPort.matchers(body.getPassword(), confirmPassword)) {
      throw new InvalidPassword("As senhas são divergentes");
    }
    return this.saveUserPort.execute(body);
  }
}