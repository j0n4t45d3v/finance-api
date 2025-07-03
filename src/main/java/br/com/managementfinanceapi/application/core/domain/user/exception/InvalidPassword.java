package br.com.managementfinanceapi.application.core.domain.user.exception;

import br.com.managementfinanceapi.application.core.domain.common.exception.BadRequestException;

public class InvalidPassword extends BadRequestException {
  public InvalidPassword(String message) {
    super(message);
  }
}