package br.com.managementfinanceapi.user.exceptions;

import br.com.managementfinanceapi.infra.error.exceptions.BadRequestException;
import br.com.managementfinanceapi.infra.error.exceptions.BaseException;

public class EmailAlreadyUsed extends BadRequestException {
  public EmailAlreadyUsed() {
    super("Email already used");
  }
}
