package com.jonatas.finance.common;

public interface ErrorCode {

    default String code() {
        if (this instanceof Enum enumValue) {
            return enumValue.name();
        }
        throw new UnsupportedOperationException("Unimplemented method code().");
    }

    String message();
}
