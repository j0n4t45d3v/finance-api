package com.jonatas.finance.infra.swagger.annotation;

import com.jonatas.finance.infra.swagger.schemas.ContractViolationResponse;
import com.jonatas.finance.infra.swagger.schemas.DomainViolationResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "400",
                description = "Bad Request",
                content = @Content(
                        schema = @Schema(implementation = ContractViolationResponse.class),
                        mediaType = MediaType.APPLICATION_JSON_VALUE
                )
        ),
        @ApiResponse(
                responseCode = "422",
                description = "Unprocessable Entity",
                content = @Content(
                        schema = @Schema(implementation = DomainViolationResponse.class),
                        mediaType = MediaType.APPLICATION_JSON_VALUE
                )
        ),
        @ApiResponse(responseCode = "500", description = "Internal Server Error"),
})
public @interface DefaultErrorResponses {
}
