package com.jonatas.finance.infra.error;

public record Error<TError>(String type, TError error) {
}
