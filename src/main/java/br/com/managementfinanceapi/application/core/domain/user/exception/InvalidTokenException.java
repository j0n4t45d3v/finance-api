package br.com.managementfinanceapi.application.core.domain.user.exception;

import br.com.managementfinanceapi.application.core.domain.common.exception.BadRequestException;

public class InvalidTokenException extends BadRequestException {
  public InvalidTokenException() {
    super("Token inválido");
  }
}