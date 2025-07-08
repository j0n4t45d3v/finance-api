package br.com.managementfinanceapi.application.core.domain.dashboard.dto;

import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;
import br.com.managementfinanceapi.application.core.domain.transaction.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DashboardTransactionDto(
    String description,
    String category,
    BigDecimal amount,
    LocalDateTime date,
    TransactionType type
) {
  public static DashboardTransactionDto of(TransactionDomain transaction){
    return new DashboardTransactionDto(
        transaction.getDescription(),
        transaction.getCategoryName(),
        transaction.getAmount(),
        transaction.getDate(),
        transaction.getType()
    );
  }
}
