package br.com.managementfinanceapi.application.port.in.user;

import br.com.managementfinanceapi.application.core.domain.user.User;

public interface FindOneUser {
  User byEmail(String email);
  User byId(Long id);
}
