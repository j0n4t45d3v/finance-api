package com.jonatas.finance.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import com.jonatas.finance.common.ErrorCode;

import java.time.LocalDateTime;
import java.time.ZoneId;

public record Response<TData, TError>(LocalDateTime timestamp,
                                      Status status,
                                      @JsonInclude(JsonInclude.Include.NON_NULL) TData data,
                                      @JsonInclude(JsonInclude.Include.NON_NULL) TError error) {

    public Response {
        if (timestamp == null) {
            timestamp = LocalDateTime.now(ZoneId.of("UTC"));
        }
    }

    public enum Status {
        OK(200),
        NOT_FOUND(404),
        UNPROCESSABLE_ENTITY(422),
        BAD_REQUEST(400),
        CREATED(201),
        CONFLICT(409),
        INTERNAL_SERVER_ERROR(500);

        private final int value;

        Status(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }
    }

    public static <TError> Response<Void, TError> ofError(TError error, Status status) {
        return new Response<>(null, status, null, error);
    }

    public static <T> Response<T, DomainError> ofError(ErrorCode error, Status status) {
        return new Response<>(null, status, null, new DomainError(error.code(), error.message()));
    }

    public static <TData> Response<TData, Void> of(TData data) {
        return Response.of(data, Status.OK);
    }

    public static <TData> Response<TData, Void> of(TData data, Status status) {
        return new Response<>(null, status, data, null);
    }

    public record DomainError(String code, String message) {}
}
