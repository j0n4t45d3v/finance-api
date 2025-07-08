package br.com.managementfinanceapi.application.core.domain.dashboard.dto;

import java.math.BigDecimal;

public record DashboardResumeAnnualDto(
    int year,
    BigDecimal totalIncome,
    BigDecimal totalExpenses,
    BigDecimal balance
) {}
