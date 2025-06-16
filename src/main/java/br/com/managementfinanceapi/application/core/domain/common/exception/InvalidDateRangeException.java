package br.com.managementfinanceapi.application.core.domain.common.exception;

public class InvalidDateRangeException extends BaseException {

  public InvalidDateRangeException(String message) {
    super(message, 400);
  }
  
}