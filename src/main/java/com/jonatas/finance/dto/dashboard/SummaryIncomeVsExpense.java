package com.jonatas.finance.dto.dashboard;

import java.math.BigDecimal;

public record SummaryIncomeVsExpense(
    BigDecimal totalIncome,
    BigDecimal totalExpense,
    BigDecimal balance
) {

}
