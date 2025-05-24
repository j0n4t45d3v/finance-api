package br.com.managementfinanceapi.application.core.domain.common.exception;

public class ConflictException extends BaseException{
  public ConflictException(String message) {
    super(message, 409);
  }
}