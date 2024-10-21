package com.management.finance.error.exceptions;

public class InternalServerErrorException extends BaseHttpException {
  public InternalServerErrorException(String message) {
    super(message, 500);
  }
  
  public InternalServerErrorException() {
    super("Error in process request in server", 500);
  }
}
