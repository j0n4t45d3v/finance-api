package com.jonatas.finance.wallet;

import com.jonatas.finance.common.ErrorCode;

public enum TransactionErrorCode implements ErrorCode {
    TRANSACTION_CANNOT_BE_CREATED_IN_THE_FUTURE("Transaction cannot be created in the future");

    private final String message;

    TransactionErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String message() {
        return this.message;
    }
}
