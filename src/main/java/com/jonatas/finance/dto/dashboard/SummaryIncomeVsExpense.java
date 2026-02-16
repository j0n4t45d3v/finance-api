package com.jonatas.finance.dto.dashboard;

import java.math.BigDecimal;

public record SummaryIncomeVsExpense(
    BigDecimal totalIncome,
    BigDecimal totalExpense,
    BigDecimal balance
) {

    public SummaryIncomeVsExpense {
        if (totalIncome == null) {
            totalIncome = BigDecimal.ZERO;
        }
        if (totalExpense == null) {
            totalExpense = BigDecimal.ZERO;
        }
        if (balance == null) {
            balance = BigDecimal.ZERO;
        }
    }
}
