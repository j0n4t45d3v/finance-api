package br.com.managementfinanceapi.application.core.domain.user.exception;

import br.com.managementfinanceapi.application.core.domain.common.exception.BaseException;

public class InvalidCredentials extends BaseException {
  public InvalidCredentials(String message) {
    super(message, 401);
  }
}