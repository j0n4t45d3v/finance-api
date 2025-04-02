package br.com.managementfinanceapi.user.usecases;

import br.com.managementfinanceapi.user.domain.User;
import br.com.managementfinanceapi.user.domain.dto.UserResponse;
import br.com.managementfinanceapi.user.exceptions.UserNotFound;
import br.com.managementfinanceapi.user.gateways.FindOneUser;
import br.com.managementfinanceapi.user.repository.UserRepository;
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
