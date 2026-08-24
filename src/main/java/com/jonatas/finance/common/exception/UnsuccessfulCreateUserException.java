package com.jonatas.finance.common.exception;

public class UnsuccessfulCreateUserException extends DomainException {
  public UnsuccessfulCreateUserException() {
    super("UNSUCCESSFUL_CREATE_USER");
  }
}
