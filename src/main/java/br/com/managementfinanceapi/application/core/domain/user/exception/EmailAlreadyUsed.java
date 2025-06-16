package br.com.managementfinanceapi.application.core.domain.user.exception;

import br.com.managementfinanceapi.application.core.domain.common.exception.ConflictException;

public class EmailAlreadyUsed extends ConflictException {
  public EmailAlreadyUsed() {
    super("E-mail já está sendo usado por outro usuário!");
  }
}