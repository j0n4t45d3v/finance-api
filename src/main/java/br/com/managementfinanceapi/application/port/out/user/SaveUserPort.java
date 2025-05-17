package br.com.managementfinanceapi.application.port.out.user;

import br.com.managementfinanceapi.application.core.domain.user.UserDomain;

public interface SaveUser {
  UserDomain execute(UserDomain user);
}
