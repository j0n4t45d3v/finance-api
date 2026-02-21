package com.jonatas.finance.dto.dashboard;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record TransactionGroupByResponse(
    String label,
    BigDecimal incomes,
    BigDecimal expenses,
    BigDecimal balance,
    Long quantity
) {

    public TransactionGroupByResponse(String label, BigDecimal incomes, BigDecimal expenses, Long quantity) {
        this(
            label,
            incomes.setScale(2, RoundingMode.HALF_UP),
            expenses.setScale(2, RoundingMode.HALF_UP),
            incomes.subtract(expenses).setScale(2, RoundingMode.HALF_UP),
            quantity
        );
    }

    public TransactionGroupByResponse(Integer year, Integer month, BigDecimal incomes, BigDecimal expenses, Long quantity) {
        this(String.format("%d-%02d", year, month), incomes, expenses, quantity);
    }
}
