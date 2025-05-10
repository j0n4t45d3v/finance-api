package br.com.managementfinanceapi.application.core.domain.transaction.dtos;

import br.com.managementfinanceapi.application.core.domain.transaction.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AddTransaction(
    BigDecimal amount,
    String description,
    LocalDateTime date,
    TransactionType type,
    Long userId,
    Long categoryId
) {}