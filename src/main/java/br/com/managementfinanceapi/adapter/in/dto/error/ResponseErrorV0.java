package br.com.managementfinanceapi.adapter.in.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

public record ResponseErrorV0<T>(
    int status,
    LocalDateTime timestamp,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String message,
    T error
) {

  public static ResponseErrorV0<String> notFound() {
    return ResponseErrorV0.notFound("Not Found");
  }

  public static ResponseErrorV0<String> notFound(String error) {
    return ResponseErrorV0.notFound(null, error);
  }

  public static ResponseErrorV0<String> notFound(String message, String error) {
    return ResponseErrorV0.of(404, message, error);
  }

  public static ResponseErrorV0<String> badRequest() {
    return ResponseErrorV0.badRequest("Bad Request");
  }

  public static <T> ResponseErrorV0<T> badRequest(String message, T error) {
    return ResponseErrorV0.of(400, message, error);
  }

  public static ResponseErrorV0<String> badRequest(String error) {
    return ResponseErrorV0.of(400, error);
  }

  public static ResponseErrorV0<String> forbidden(String error) {
    return ResponseErrorV0.forbidden(null, error);
  }

  public static ResponseErrorV0<String> forbidden(String message, String error) {
    return ResponseErrorV0.of(403, message, error);
  }

  public static ResponseErrorV0<String> unauthorized(String error) {
    return ResponseErrorV0.unauthorized(null, error);
  }

  public static ResponseErrorV0<String> unauthorized(String message,String error) {
    return ResponseErrorV0.of(401, message, error);
  }

  public static ResponseErrorV0<String> internalServerError() {
    return ResponseErrorV0.internalServerError("Internal Server Error");
  }

  public static ResponseErrorV0<String> internalServerError(String error) {
    return ResponseErrorV0.internalServerError("Server Error", error);
  }

  public static ResponseErrorV0<String> internalServerError(String message, String error) {
    return ResponseErrorV0.of(500, message, error);
  }

  public static <T> ResponseErrorV0<T> of(int statusCode, T error) {
    return ResponseErrorV0.of(statusCode, null, error);
  }

  public static <T> ResponseErrorV0<T> of(int statusCode, String message, T error) {
    return new ResponseErrorV0<>(statusCode, LocalDateTime.now(), message, error);
  }
}