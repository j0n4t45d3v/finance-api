package com.management.finance.error.exceptions;

public class BadRequestException extends BaseHttpException {
  public BadRequestException(String message) {
    super(message, 400);
  }
}
