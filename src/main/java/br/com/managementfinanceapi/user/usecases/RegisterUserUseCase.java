package br.com.managementfinanceapi.user.usecases;

import br.com.managementfinanceapi.user.domain.User;
import br.com.managementfinanceapi.user.domain.dto.CreateUser;
import br.com.managementfinanceapi.user.exceptions.EmailAlreadyUsed;
import br.com.managementfinanceapi.user.exceptions.InvalidPassword;
import br.com.managementfinanceapi.user.gateways.RegisterUser;
import br.com.managementfinanceapi.user.repository.UserRepository;
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
