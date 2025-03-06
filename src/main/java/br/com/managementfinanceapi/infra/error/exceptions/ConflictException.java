package br.com.managementfinanceapi.infra.error.exceptions;

public class ConflictException extends BaseException{
  public ConflictException(String message) {
    super(message, 409);
  }
}
