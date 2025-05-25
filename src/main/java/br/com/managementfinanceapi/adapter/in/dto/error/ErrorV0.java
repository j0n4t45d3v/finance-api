package br.com.managementfinanceapi.adapter.in.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public record ErrorV0<T>(
    @JsonInclude(Include.NON_NULL)
    String message,
    @JsonInclude(Include.NON_NULL)
    T error
) {
  public static <T> ErrorV0<T> of(String message) {
    return new ErrorV0<>(message, null);
  }

  public static <T> ErrorV0<T> of(T data) {
    return new ErrorV0<>(null, data);
  }
}