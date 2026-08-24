package com.jonatas.finance.auth;

import com.jonatas.finance.common.exception.DomainException;

public class EmailInvalidException extends DomainException {
  public EmailInvalidException(String message) {
    super(message);
  }
}
