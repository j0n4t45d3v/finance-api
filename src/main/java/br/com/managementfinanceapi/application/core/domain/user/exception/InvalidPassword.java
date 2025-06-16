package br.com.managementfinanceapi.application.core.domain.user.exception;

import br.com.managementfinanceapi.application.core.domain.common.exception.BadRequestException;

public class InvalidPassword extends BadRequestException {
  public InvalidPassword(String message) {
    super(message);
  }

  public InvalidPassword() {
    super("A senha deve ter pelo menos de 8 á 20 caracteres e conter números e letras minusculas e maiusculas!");
  }
}