package com.jonatas.finance.infra.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;

import java.time.LocalDateTime;
import java.time.ZoneId;

public record Response<TData, TError>(
        LocalDateTime timestamp,
        Status status,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        TData data,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        TError error

) {

    public Response {
        if (timestamp == null) {
            timestamp = LocalDateTime.now(ZoneId.of("UTC"));
        }
    }

    public enum Status{
        OK(200),
        NOT_FOUND(404),
        UNPROCESSABLE_ENTITY(422),
        BAD_REQUEST(400),
        CREATED(201);

        private final int status;
        Status(int status) {
            this.status = status;
        }

        @JsonValue
        public int getStatus() {
            return status;
        }
    }

    public static <TError> Response<Void, TError> ofError(TError error, Status status) {
        return new Response(null, status, null, error);
    }

    public static <TData> Response<TData, Void> of(TData data) {
        return Response.of(data, Status.OK);
    }

    public static <TData> Response<TData, Void> of(TData data, Status status) {
        return new Response<>(null, status, data, null);
    }
}
