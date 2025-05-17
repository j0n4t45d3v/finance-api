package br.com.managementfinanceapi.infra.error.exceptions.user;

import br.com.managementfinanceapi.infra.error.exceptions.BadRequestException;

public class InvalidCredentialsException extends BadRequestException {
  public InvalidCredentialsException(String message) {
    super(message);
  }
}
