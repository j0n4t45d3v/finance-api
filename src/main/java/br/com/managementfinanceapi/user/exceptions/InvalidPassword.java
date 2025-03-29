package br.com.managementfinanceapi.user.exceptions;

import br.com.managementfinanceapi.infra.error.exceptions.BadRequestException;

public class InvalidPassword extends BadRequestException {
  public InvalidPassword(String message) {
    super(message);
  }

  public InvalidPassword() {
    super("A senha deve ter pelomenos de 8 á 20 caracteres e conter numeros e letras minusculas e maiusculas!");
  }
}
