package br.com.managementfinanceapi.application.port.out.security;

import br.com.managementfinanceapi.application.core.domain.user.dvo.Password;

public interface HashPasswordPort {

  String encode(Password password);
  boolean matchers(String encoderPassword, String comparePassword);  

}