package br.com.managementfinanceapi.application.core.usecase.user;

import br.com.managementfinanceapi.application.core.domain.user.User;
import br.com.managementfinanceapi.infra.error.exceptions.user.UserNotFound;
import br.com.managementfinanceapi.application.port.in.user.FindOneUser;
import br.com.managementfinanceapi.adapter.out.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FindOneUseCase implements FindOneUser {

  private final UserRepository repository;

  public FindOneUseCase(UserRepository repository) {
    this.repository = repository;
  }

  @Override
  public User byEmail(String email) {
    Optional<User> userFound = this.repository.findByEmail(email);
    return userFound.orElseThrow(UserNotFound::new);
  }

  @Override
  public User byId(Long id) {
    Optional<User> userFound = this.repository.findById(id);
    return userFound.orElseThrow(UserNotFound::new);
  }

}
