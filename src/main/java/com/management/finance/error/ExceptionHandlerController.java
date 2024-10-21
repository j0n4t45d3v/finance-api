package com.management.finance.error;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.management.finance.error.exceptions.BaseHttpException;
import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(BaseHttpException.class)
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "409", description = "Conflict"),
    })
    public ResponseEntity<ErrorResponseV0> handleBaseHttpException(BaseHttpException ex) {
        var statusCode = ex.getCode();
        var errorResponse = new ErrorResponseV0(
            statusCode,
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(statusCode).body(errorResponse);
    }
    
    @ExceptionHandler(RuntimeException.class)
    @ApiResponse(responseCode = "400", description = "Bad Request")
    public ResponseEntity<ErrorResponseV0> handleRuntimeException(RuntimeException ex) {
        var errorResponse = new ErrorResponseV0(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public ResponseEntity<ErrorResponseV0> handleGenericException(Exception ex) {
        var errorResponse = new ErrorResponseV0(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
