package com.jonatas.finance.auth;

import com.jonatas.finance.common.ErrorCode;

public enum AuthErrorCode implements ErrorCode {
    PASSWORD_MISMATCH("Password mismatch"),
    FAIL_REGISTER("Fail register");

    private final String message;

    AuthErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String message() {
        return message;
    }
}
