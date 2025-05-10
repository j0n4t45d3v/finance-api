package br.com.managementfinanceapi.infra.error.exceptions.auth;

import br.com.managementfinanceapi.infra.error.exceptions.BadRequestException;

public class InvalidTokenException extends BadRequestException {
  public InvalidTokenException() {
    super("Token invalido");
  }
}
