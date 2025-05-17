package br.com.managementfinanceapi.application.port.in.user;

import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.application.core.domain.user.dto.auth.Login;
import org.springframework.security.core.userdetails.UserDetails;

public interface LoginGateway {
  UserDomain execute(Login login);
}