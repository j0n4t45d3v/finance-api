package com.jonatas.finance.dto.account;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateTransactionRequest(
    String description,

    @NotNull
    @DecimalMin("0.1")
    BigDecimal amount,

    @NotNull
    LocalDateTime datetime,

    @NotNull
    Long categoryId,

    @NotNull
    Long accountId
) {
}
