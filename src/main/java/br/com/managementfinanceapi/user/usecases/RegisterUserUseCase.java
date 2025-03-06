package br.com.managementfinanceapi.user.usecases;

import br.com.managementfinanceapi.user.domain.User;
import br.com.managementfinanceapi.user.domain.dto.CreateUser;
import br.com.managementfinanceapi.user.domain.dto.UserResponse;
import br.com.managementfinanceapi.user.exceptions.EmailAlreadyUsed;
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
  public UserResponse execute(CreateUser body) {
    if(this.repository.findByEmail(body.email()).isPresent()) {
      throw new EmailAlreadyUsed();
    }
    User user = new User(body);
    User userSaved = this.repository.save(user);
    return UserResponse.from(userSaved);
  }
}
