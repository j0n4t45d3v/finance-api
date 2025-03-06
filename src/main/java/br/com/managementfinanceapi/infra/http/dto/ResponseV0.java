package br.com.managementfinanceapi.infra.http.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

public record ResponseV0<T>(
    int status,
    LocalDateTime timestamp,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String message,
    T data
) {
  public static <T> ResponseV0<T> created(T data) {
    return ResponseV0.created( null, data);
  }

  public static <T> ResponseV0<T> created(String message, T data) {
    return new ResponseV0<>(201, LocalDateTime.now(), message, data);
  }

  public static <T> ResponseV0<T> ok(T data) {
    return ResponseV0.ok( null, data);
  }

  public static <T> ResponseV0<T> ok(String message, T data) {
    return new ResponseV0<>(200, LocalDateTime.now(), message, data);
  }

  public static ResponseV0<ErrorV0> error(int statusError, Exception error) {
    return ResponseV0.error(statusError, error.getMessage());
  }

  public static ResponseV0<ErrorV0> error(int statusError, ErrorV0 error) {
    return new ResponseV0<>(statusError, LocalDateTime.now(), null, error);
  }

  public static ResponseV0<ErrorV0> error(int statusError, String messageError) {
    ErrorV0 errorV0 = ErrorV0.of(messageError);
    return new ResponseV0<>(statusError, LocalDateTime.now(),null, errorV0);
  }
}