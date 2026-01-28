package com.jonatas.finance.infra.swagger.schemas;

import com.jonatas.finance.infra.dto.Response;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Error domain violation")
public class DomainViolationResponse {

    public LocalDateTime timestamp;
    @Schema(example = "422")
    public Response.Status status;
    public ErrorData data;

    public static class ErrorData {
        @Schema(example = "domain_violation")
        public String type;
        @Schema(example = "Invalid e-mail")
        public String message;
    }
}
