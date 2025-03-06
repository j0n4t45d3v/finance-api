package br.com.managementfinanceapi.infra.error.exceptions;

public class BadRequestException extends BaseException{

  public BadRequestException(String message) {
    super(message, 400);
  }
}
