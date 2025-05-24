package br.com.managementfinanceapi.application.core.domain.common.exception;

public class BaseException extends RuntimeException {

  private final int code;

  public BaseException(String message, int code) {
    super(message);
    this.code = code;
  }

  public int getCode() {
    return this.code;
  }
}