package com.jonatas.finance.domain.exception;

public class UnsuccessfulCreateUserException extends DomainException {
    public UnsuccessfulCreateUserException() {
        super("UNSUCCESSFUL_CREATE_USER");
    }
}
