package com.jonatas.finance.wallet;

import com.jonatas.finance.common.ErrorCode;

public enum WalletErrorCode implements ErrorCode {

    WALLET_NOT_FOUND("Wallet not found"),
    WALLET_WITH_THIS_NAME_ALREADY_EXISTS("Wallet with this name already exists"),
    MAIN_WALLET_ALREADY_EXISTS("Main wallet already exists");

    private final String message;

    WalletErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String message() {
        return message;
    }
}
