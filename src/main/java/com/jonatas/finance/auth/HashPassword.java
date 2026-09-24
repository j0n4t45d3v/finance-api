package com.jonatas.finance.auth;

import com.jonatas.finance.common.exception.DomainException;

import java.util.Objects;

public record HashPassword(String value) {

    public HashPassword {
        validate(value);
    }

    private void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new DomainException("Password is required");
        }
    }

    public static HashPassword of(String value) {
        return Objects.nonNull(value) ? new HashPassword(value)
                                      : null;
    }
}
