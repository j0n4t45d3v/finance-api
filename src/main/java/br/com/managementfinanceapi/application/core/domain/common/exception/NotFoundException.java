package br.com.managementfinanceapi.application.core.domain.common.exception;

public class NotFoundException extends BaseException{

  public NotFoundException(String message) {
    super(message, 404);
  }
}