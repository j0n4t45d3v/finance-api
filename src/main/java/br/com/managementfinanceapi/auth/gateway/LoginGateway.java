package br.com.managementfinanceapi.auth.gateway;

import br.com.managementfinanceapi.auth.controller.AuthControllerV1.Token;
import br.com.managementfinanceapi.auth.domain.dto.Login;

public interface LoginGateway {
  Token execute(Login login); 
}