package br.com.managementfinanceapi.application.core.usecase.user;

import br.com.managementfinanceapi.application.core.domain.user.User;
import br.com.managementfinanceapi.application.core.domain.user.dto.CreateUser;
import br.com.managementfinanceapi.infra.error.exceptions.user.EmailAlreadyUsed;
import br.com.managementfinanceapi.infra.error.exceptions.user.InvalidPassword;
import br.com.managementfinanceapi.application.port.in.user.RegisterUser;
import br.com.managementfinanceapi.adapter.out.repository.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserUseCase implements RegisterUser {

  private final UserRepository repository;

  public RegisterUserUseCase(UserRepository repository) {
    this.repository = repository;
  }

  @Override
  public User execute(CreateUser body) {
    if (this.repository.findByEmail(body.email()).isPresent()) {
      throw new EmailAlreadyUsed();
    }
    User user = new User(body);

    if(user.differentPassword(body.confirmPassword())) {
      throw new InvalidPassword("Confirmar senha está diferente da senha");
    }
    return this.repository.save(user);
  }
}
