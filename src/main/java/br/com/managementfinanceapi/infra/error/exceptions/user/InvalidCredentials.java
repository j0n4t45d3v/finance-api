package br.com.managementfinanceapi.infra.error.exceptions.user;

import br.com.managementfinanceapi.infra.error.exceptions.BadRequestException;

public class InvalidCredentials extends BadRequestException {
  public InvalidCredentials(String message) {
    super(message);
  }
}
