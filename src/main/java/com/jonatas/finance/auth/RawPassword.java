package com.jonatas.finance.auth;

import com.jonatas.finance.common.exception.DomainException;

import java.util.Objects;

public record RawPassword(String value) {

    public RawPassword {
        validate(value);
    }

    private void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new DomainException("Password is required");
        } else if (value.length() < 10) {
            throw new DomainException("Password must be at least 10 characters long");
        }
    }

    public static RawPassword of(String value) {
        return Objects.nonNull(value) ? new RawPassword(value)
                                      : null;
    }
}
