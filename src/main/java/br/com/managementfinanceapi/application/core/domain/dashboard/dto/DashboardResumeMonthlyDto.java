package br.com.managementfinanceapi.application.core.domain.dashboard.dto;

import java.math.BigDecimal;
import java.time.YearMonth;

public record DashboardResumeMonthlyDto(
    YearMonth month,
    BigDecimal income,
    BigDecimal expenses
) {
}
