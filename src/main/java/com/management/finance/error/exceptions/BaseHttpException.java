package com.management.finance.error.exceptions;

public class BaseHttpException extends RuntimeException {
  private final int code;
  public BaseHttpException(String message, int code) {
    super(message);
    this.code = code;
  }
  public int getCode() {
    return code;
  }  
}
