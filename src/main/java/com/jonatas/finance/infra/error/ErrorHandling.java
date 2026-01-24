package com.jonatas.finance.infra.error;

import com.jonatas.finance.domain.exception.DomainException;
import com.jonatas.finance.infra.dto.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class ErrorHandling {

    private static final Logger log = LoggerFactory.getLogger(ErrorHandling.class);

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Response<Error>> domainError(DomainException error) {
        Error domainViolation = new Error("domain_violation", error.getMessage());
        return ResponseEntity.unprocessableContent()
                .body(Response.of(domainViolation, Response.Status.UNPROCESSABLE_ENTITY));
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
