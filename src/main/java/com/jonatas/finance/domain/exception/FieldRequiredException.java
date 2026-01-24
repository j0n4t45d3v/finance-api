package com.jonatas.finance.domain.exception;

public class FieldRequiredException extends DomainException {

    public FieldRequiredException(String field) {
        super("{%s} is required".formatted(field));
    }
}
