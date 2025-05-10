package br.com.managementfinanceapi.infra.error.exceptions.user;

import br.com.managementfinanceapi.infra.error.exceptions.NotFoundException;

public class UserNotFound extends NotFoundException {

  public UserNotFound() {
    super("User not found");
  }
}
