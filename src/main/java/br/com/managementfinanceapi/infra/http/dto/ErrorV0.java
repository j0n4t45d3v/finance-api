package br.com.managementfinanceapi.infra.http.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public record ErrorV0(
    String message
) {
  public static ErrorV0 of(String message) {
    return new ErrorV0(message);
  }
}