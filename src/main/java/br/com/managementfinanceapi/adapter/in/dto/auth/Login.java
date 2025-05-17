package br.com.managementfinanceapi.adapter.in.dto.auth;

import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.application.core.domain.user.dvo.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record Login(
  @NotNull(message = "E-mail é um campo obrigátorio")
  @Email(message = "E-mail inválido")
  String email,
  String password
) { 
  public UserDomain tDomain() {
    return new UserDomain(this.email(), Password.from(this.password()));
  }
}