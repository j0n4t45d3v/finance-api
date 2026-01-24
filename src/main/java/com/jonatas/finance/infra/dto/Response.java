package com.jonatas.finance.infra.dto;

import java.time.LocalDateTime;
import java.time.ZoneId;

public record Response<TData>(
        LocalDateTime timestamp,
        Status status,
        TData data

) {

    public Response {
        if (timestamp == null) {
            timestamp = LocalDateTime.now(ZoneId.of("UTC"));
        }
    }

    public enum Status{
        NOT_FOUND,
        UNPROCESSABLE_ENTITY,
        BAD_REQUEST,
        OK,
        CREATED
    }


    public static <TData> Response<TData> of(TData data) {
        return Response.of(data, Status.OK);
    }

    public static <TData> Response<TData> of(TData data, Status status) {
        return new Response<>(null, status, data);
    }
}
