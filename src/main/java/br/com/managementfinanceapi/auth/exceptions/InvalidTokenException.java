package br.com.managementfinanceapi.auth.exceptions;

import br.com.managementfinanceapi.infra.error.exceptions.BadRequestException;

public class InvalidTokenException extends BadRequestException {
  public InvalidTokenException() {
    super("Token invalido");
  }
}
