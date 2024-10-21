package com.management.finance.error.exceptions;

public class ConflictException extends BaseHttpException {
    public ConflictException(String message) {
        super(message, 409);
    }
}
