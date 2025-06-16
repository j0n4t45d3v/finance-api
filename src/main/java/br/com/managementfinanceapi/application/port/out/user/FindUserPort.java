package br.com.managementfinanceapi.application.port.out.user;

import br.com.managementfinanceapi.application.core.domain.user.UserDomain;

import java.util.Optional;

public interface FindUserPort {
  Optional<UserDomain> byEmail(String email);
  Optional<UserDomain> byId(Long id);
}
