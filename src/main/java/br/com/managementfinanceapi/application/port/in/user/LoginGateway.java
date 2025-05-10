package br.com.managementfinanceapi.application.port.in.user;

import br.com.managementfinanceapi.application.core.domain.user.dto.auth.Login;
import org.springframework.security.core.userdetails.UserDetails;

public interface LoginGateway {
  UserDetails execute(Login login);
}