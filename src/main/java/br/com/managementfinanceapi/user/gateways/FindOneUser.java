package br.com.managementfinanceapi.user.gateways;

import br.com.managementfinanceapi.user.domain.User;

public interface FindOneUser {
  User byEmail(String email);
  User byId(Long id);
}
