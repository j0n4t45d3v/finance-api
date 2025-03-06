package br.com.managementfinanceapi.user.exceptions;

import br.com.managementfinanceapi.infra.error.exceptions.NotFoundException;

public class UserNotFound extends NotFoundException {

  public UserNotFound() {
    super("User not found");
  }
}
