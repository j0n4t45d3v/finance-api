package br.com.managementfinanceapi.application.port.out.user;

import br.com.managementfinanceapi.application.core.domain.user.UserDomain;

public interface SaveUserPort {
  UserDomain execute(UserDomain user);
}
