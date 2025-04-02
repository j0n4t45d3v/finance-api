package br.com.managementfinanceapi.auth.gateway;

import br.com.managementfinanceapi.auth.domain.dto.Login;
import org.springframework.security.core.userdetails.UserDetails;

public interface LoginGateway {
  UserDetails execute(Login login);
}