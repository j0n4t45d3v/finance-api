package br.com.managementfinanceapi.adapter.out.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.managementfinanceapi.application.core.domain.user.dvo.Password;
import br.com.managementfinanceapi.application.port.out.security.HashPasswordPort;

@Component
public class HashPassword implements HashPasswordPort {

  private final PasswordEncoder passwordEncoder;

  public HashPassword(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public String encode(Password password) {
    return this.passwordEncoder.encode(password.getValue());
  }

  @Override
  public boolean matchers(String encoderPassword, String comparePassword) {
    return this.passwordEncoder.matches(encoderPassword, comparePassword);
  }

}