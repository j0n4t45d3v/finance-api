package br.com.managementfinanceapi.application.port.in.user;

import br.com.managementfinanceapi.application.core.domain.user.UserDomain;

public interface LoginPort {
  UserDomain execute(UserDomain login);
}