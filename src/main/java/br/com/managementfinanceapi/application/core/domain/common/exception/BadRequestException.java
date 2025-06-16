package br.com.managementfinanceapi.application.core.domain.common.exception;

public class BadRequestException extends BaseException{

  public BadRequestException(String message) {
    super(message, 400);
  }
}