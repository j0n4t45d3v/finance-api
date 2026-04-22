package com.jonatas.finance.dto.account;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Requisição para criar uma transação financeira")
public record CreateTransactionRequest(
    @Schema(example = "Compra de frutas")
    String description,

    @Schema(example = "200.50")
    @NotNull
    @DecimalMin("0.1")
    BigDecimal amount,

    @NotNull
    LocalDateTime datetime,

    @Schema(example = "1")
    @NotNull
    Long categoryId,

    @Schema(example = "1")
    @NotNull
    Long accountId
) {
}
