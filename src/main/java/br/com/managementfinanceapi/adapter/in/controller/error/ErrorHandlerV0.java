package br.com.managementfinanceapi.adapter.in.controller.error;


import br.com.managementfinanceapi.adapter.in.dto.error.ResponseErrorV0;
import br.com.managementfinanceapi.application.core.domain.common.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@ControllerAdvice
public class ErrorHandlerV0 {

  private static final Logger log = LoggerFactory.getLogger(ErrorHandlerV0.class);

  private enum ErrorCategory {
    VALIDATION("Validation Error"),
    RESOURCE("Resource Error"),
    DOMAIN("Domain Error");

    private final String value;
    ErrorCategory(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }

  @ExceptionHandler(BaseException.class)
  public ResponseEntity<ResponseErrorV0<String>> handleBaseException(BaseException e) {
    ResponseErrorV0<String> responseError =
        ResponseErrorV0.of(e.getCode(), ErrorCategory.DOMAIN.getValue(),  e.getMessage());
    return ResponseEntity.status(e.getCode()).body(responseError);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResponseErrorV0<List<String>>> handleSpringValidation(MethodArgumentNotValidException e) {
    List<String> errors = e.getAllErrors()
        .stream()
        .map(error -> {
          if(error instanceof FieldError fieldError){
            return String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage());
          }
          return error.getDefaultMessage();
        })
        .toList();
    ResponseErrorV0<List<String>> responseError =
        ResponseErrorV0.badRequest(ErrorCategory.VALIDATION.getValue(), errors);
    return ResponseEntity.badRequest().body(responseError);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ResponseErrorV0<String>> handleNoResource(NoResourceFoundException e) {
    ResponseErrorV0<String> responseError =
        ResponseErrorV0.notFound(ErrorCategory.RESOURCE.getValue(), "Route " + e.getResourcePath() + " Not Found ");
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ResponseErrorV0<String>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException e) {
    ResponseErrorV0<String> responseError =
        ResponseErrorV0.of(405, ErrorCategory.RESOURCE.getValue(), e.getMessage());
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(responseError);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResponseErrorV0<String>> handleGenericException(Exception e) {
    ResponseErrorV0<String> responseError = ResponseErrorV0.internalServerError("An unexpected error occurred.");
    log.error("Internal Server Error={}", e);
    return ResponseEntity.internalServerError().body(responseError);
  }
}