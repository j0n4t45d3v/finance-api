package br.com.managementfinanceapi.application.core.domain.user.exception;

import br.com.managementfinanceapi.application.core.domain.common.exception.BadRequestException;

public class InvalidCredentials extends BadRequestException {
  public InvalidCredentials(String message) {
    super(message);
  }
}