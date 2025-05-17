package br.com.managementfinanceapi.application.port.in.user;

import br.com.managementfinanceapi.application.core.domain.user.UserDomain;

public interface FindOneUser {
  UserDomain byEmail(String email);
  UserDomain byId(Long id);
}
