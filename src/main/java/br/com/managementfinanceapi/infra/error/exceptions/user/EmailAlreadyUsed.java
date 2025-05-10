package br.com.managementfinanceapi.infra.error.exceptions.user;

import br.com.managementfinanceapi.infra.error.exceptions.BadRequestException;

public class EmailAlreadyUsed extends BadRequestException {
  public EmailAlreadyUsed() {
    super("Email already used");
  }
}
