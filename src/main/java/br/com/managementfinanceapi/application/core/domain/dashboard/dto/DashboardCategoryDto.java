package br.com.managementfinanceapi.application.core.domain.dashboard.dto;

import br.com.managementfinanceapi.application.core.domain.transaction.enums.TransactionType;

import java.math.BigDecimal;

public record DashboardCategoryDto(
    String category,
    BigDecimal amount,
    TransactionType type
) {}
