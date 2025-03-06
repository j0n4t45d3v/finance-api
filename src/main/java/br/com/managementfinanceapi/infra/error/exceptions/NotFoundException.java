package br.com.managementfinanceapi.infra.error.exceptions;

public class NotFoundException extends BaseException{

  public NotFoundException(String message) {
    super(message, 404);
  }
}
