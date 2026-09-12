package com.jonatas.finance.wallet;

import com.jonatas.finance.common.ErrorCode;

public enum CategoryErrorCode implements ErrorCode {
    ALREADY_EXISTS_CATEGORY_WITH_NAME("Already exists category with same name");

    private final String message;

    CategoryErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String message() {
        return message;
    }

}
