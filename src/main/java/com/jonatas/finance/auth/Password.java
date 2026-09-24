package com.jonatas.finance.auth;

import java.util.Objects;

@Deprecated
public record Password(String value) {

    public static Password of(String value) {
        return Objects.nonNull(value) ? new Password(value) : null;
    }

}
