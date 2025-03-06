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
  public UserResponse byEmail(String email) {
    Optional<User> userFound = this.repository.findByEmail(email);
    return this.normalizeResult(userFound);
  }

  @Override
  public UserResponse byId(Long id) {
    Optional<User> userFound = this.repository.findById(id);
    return this.normalizeResult(userFound);
  }

  private UserResponse normalizeResult(Optional<User> optionalUser) {
    return optionalUser
        .map(User::toResponse)
        .orElseThrow(UserNotFound::new);
  }

}
