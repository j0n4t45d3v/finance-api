package br.com.managementfinanceapi.infra.error.exceptions.user;

import br.com.managementfinanceapi.infra.error.exceptions.BadRequestException;

public class InvalidPassword extends BadRequestException {
  public InvalidPassword(String message) {
    super(message);
  }

  public InvalidPassword() {
    super("A senha deve ter pelo menos de 8 á 20 caracteres e conter numeros e letras minusculas e maiusculas!");
  }
}
