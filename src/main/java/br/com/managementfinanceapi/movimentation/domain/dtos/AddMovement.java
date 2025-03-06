package br.com.managementfinanceapi.movimentation.domain.dtos;

import br.com.managementfinanceapi.movimentation.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AddMovement(
    BigDecimal amount,
    String description,
    LocalDateTime date,
    TransactionType type,
    Long userId,
    Long categoryId
) {}