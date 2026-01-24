package com.jonatas.finance.domain.dvo.user;

import com.jonatas.finance.domain.exception.EmailInvalidException;

import java.util.regex.Pattern;

public record Email(String value) {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9.]+@[a-z]+\\.[a-zA-Z.]+$");

    public Email {
        if (value == null || value.isBlank()) {
            throw new EmailInvalidException("e-mail is not be empty");
        }
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new EmailInvalidException("Invalid e-mail");
        }
    }
}
