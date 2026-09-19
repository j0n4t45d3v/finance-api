package com.jonatas.finance.common;

public class Result<T> {

    private final T value;
    private final ErrorCode error;

    private Result(T value, ErrorCode error) {
        this.value = value;
        this.error = error;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(value, null);
    }

    public static Result<Void> successVoid() {
        return new Result<>(null, null);
    }

    public static <T> Result<T> failure(ErrorCode error) {
        return new Result<>(null, error);
    }

    public T get() {
        if (isFailure()) {
            throw new IllegalStateException("Cannot get value from a failed result");
        }
        return this.value;
    }

    public ErrorCode getError() {
        if (!isFailure()) {
            throw new IllegalStateException("Cannot get error message from a successful result");
        }
        return this.error;
    }

    public boolean isFailure() {
        return this.error != null;
    }
}
