package br.com.managementfinanceapi.infra.error.exceptions;

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
