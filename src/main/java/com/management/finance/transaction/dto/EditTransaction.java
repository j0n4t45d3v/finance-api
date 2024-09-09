package com.management.finance.transaction.dto;

import com.management.finance.transaction.enums.TypeTransaction;

public record EditTransaction(
        Double amount,
        TypeTransaction type
) {
}
