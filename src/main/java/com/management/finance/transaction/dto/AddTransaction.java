package com.management.finance.transaction.dto;

import com.management.finance.transaction.enums.TypeTransaction;

public record AddTransaction(
        Double amount,
        TypeTransaction type
) {
}
