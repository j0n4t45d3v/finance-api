package br.com.managementfinanceapi.application.core.domain.user.exception;

import br.com.managementfinanceapi.application.core.domain.common.exception.BadRequestException;

public class EmailAlreadyUsed extends BadRequestException {
  public EmailAlreadyUsed() {
    super("Email já está sendo usado por outro usuário!");
  }
}