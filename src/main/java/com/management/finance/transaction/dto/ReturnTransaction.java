package com.management.finance.transaction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.management.finance.transaction.Transaction;
import com.management.finance.transaction.enums.TypeTransaction;
import com.management.finance.user.dto.ReturnUser;

import java.time.LocalDateTime;

public record ReturnTransaction(
        Double amount,
        TypeTransaction type,
        @JsonProperty(value = "created_at")
        LocalDateTime createdAt
) {
    public ReturnTransaction(Transaction transaction) {
        this(transaction.getAmount(), transaction.getType(), transaction.getCreatedAt());
    }
}
