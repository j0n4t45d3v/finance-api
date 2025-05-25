package br.com.managementfinanceapi.application.core.domain.user.exception;

import br.com.managementfinanceapi.application.core.domain.common.exception.NotFoundException;

public class UserNotFound extends NotFoundException {
  public UserNotFound() {
    super("Usuario não encontrado!");
  }
}