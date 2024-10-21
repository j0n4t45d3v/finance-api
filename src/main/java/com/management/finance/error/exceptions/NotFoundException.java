package com.management.finance.error.exceptions;

public class NotFoundException extends BaseHttpException {
  public NotFoundException(String message) {
    super(message, 404);
  }
}
