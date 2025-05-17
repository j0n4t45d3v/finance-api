package br.com.managementfinanceapi.application.core.usecase.user;

import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.application.port.out.user.FindUserPort;
import br.com.managementfinanceapi.infra.error.exceptions.user.UserNotFound;
import br.com.managementfinanceapi.application.port.in.user.SearchUserPort;

import java.util.Optional;

public class SearchUseCase implements SearchUserPort {

  private final FindUserPort findUserPort;

  public SearchUseCase(FindUserPort findUserPort) {
    this.findUserPort = findUserPort;
  }

  @Override
  public UserDomain byEmail(String email) {
    Optional<UserDomain> userFound = this.findUserPort.byEmail(email);
    return userFound.orElseThrow(UserNotFound::new);
  }

  @Override
  public UserDomain byId(Long id) {
    Optional<UserDomain> userFound = this.findUserPort.byId(id);
    return userFound.orElseThrow(UserNotFound::new);
  }

}
