package br.com.managementfinanceapi.adapter.in.controller.error;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import br.com.managementfinanceapi.adapter.in.dto.ResponseV0;
import br.com.managementfinanceapi.adapter.in.dto.error.ErrorV0;
import br.com.managementfinanceapi.application.core.domain.common.exception.BaseException;

@ControllerAdvice
public class ErrorHandlerV0 {

  private static final Logger log = LoggerFactory.getLogger(ErrorHandlerV0.class);

  @ExceptionHandler(BaseException.class)
  public ResponseEntity<ResponseV0<ErrorV0<?>>> handleBaseException(BaseException e) {
    ResponseV0<ErrorV0<?>> responseError = ResponseV0.error(e.getCode(), e.getMessage());
    return ResponseEntity.status(e.getCode()).body(responseError);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResponseV0<ErrorV0<List<String>>>> handleSpringValidation(MethodArgumentNotValidException e) {
    List<String> erros = e.getAllErrors()
        .stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .toList();
    ResponseV0<ErrorV0<List<String>>> responseError = ResponseV0.error(400, erros);
    return ResponseEntity.badRequest().body(responseError);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<Void> handleNoResource(NoResourceFoundException e) {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ResponseV0<ErrorV0<?>>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException e) {
    ResponseV0<ErrorV0<?>> responseError = ResponseV0.error(405, e.getMessage());
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(responseError);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResponseV0<ErrorV0<?>>> handleGenericException(Exception e) {
    ResponseV0<ErrorV0<?>> responseError = ResponseV0.error(500,"Internal Server Error");
    log.error("Fatal={}", e.getMessage());
    return ResponseEntity.internalServerError().body(responseError);
  }
}