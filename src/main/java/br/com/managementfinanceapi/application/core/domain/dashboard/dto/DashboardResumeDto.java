package br.com.managementfinanceapi.application.core.domain.dashboard.dto;

import br.com.managementfinanceapi.application.core.domain.common.dvo.DateRange;

import java.math.BigDecimal;

public record DashboardResumeDto(
    BigDecimal income,
    BigDecimal expenses,
    BigDecimal balance,
    DateRange period
) {}
