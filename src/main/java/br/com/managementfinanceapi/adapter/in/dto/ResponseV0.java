package br.com.managementfinanceapi.adapter.in.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

public record ResponseV0<T>(
    int status,
    LocalDateTime timestamp,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    T data
) {
  public static <T> ResponseV0<T> created(T data) {
    return ResponseV0.of(201, data);
  }

  public static <T> ResponseV0<T> ok(T data) {
    return ResponseV0.of(200, data);
  }

  public static <T> ResponseV0<T> of(int statusCode, T data) {
    return new ResponseV0<>(200, LocalDateTime.now(), data);
  }
}