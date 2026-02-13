package com.jonatas.finance.infra.error;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.jonatas.finance.domain.exception.DomainException;
import com.jonatas.finance.dto.Response;

@RestControllerAdvice
public class ErrorHandling {

    private static final Logger log = LoggerFactory.getLogger(ErrorHandling.class);

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Response<Void, Error<String>>> domainError(DomainException error) {
        Error<String> domainViolation = new Error<>("domain_violation", error.getMessage());
        return ResponseEntity.unprocessableContent()
                .body(Response.ofError(domainViolation, Response.Status.UNPROCESSABLE_ENTITY));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<Void,Error<Map<String,String>>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        Error<Map<String,String>> contractViolation = new Error<>("contract_violation", errors);
        return ResponseEntity.badRequest()
            .body(Response.ofError(contractViolation, Response.Status.BAD_REQUEST));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Void> notFoundResource(NoResourceFoundException error) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Void> methodNotAllowed(HttpRequestMethodNotSupportedException error) {
        return ResponseEntity.status(405).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> unexpectedErrors(Exception error) {
        log.error("Internal Error", error);
        return ResponseEntity.internalServerError().build();
    }

}
