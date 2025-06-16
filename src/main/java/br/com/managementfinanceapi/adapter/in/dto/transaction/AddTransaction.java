package br.com.managementfinanceapi.adapter.in.dto.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.managementfinanceapi.application.core.domain.category.CategoryDomain;
import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;
import br.com.managementfinanceapi.application.core.domain.transaction.enums.TransactionType;
import br.com.managementfinanceapi.application.core.domain.user.UserDomain;

public record AddTransaction(
    BigDecimal amount,
    String description,
    LocalDateTime date,
    TransactionType type,
    Long userId,
    Long categoryId) {
  public TransactionDomain toDomain() {

    CategoryDomain category = new CategoryDomain(this.categoryId);
    return new TransactionDomain(
        null,
        this.amount,
        this.type,
        this.description,
        this.date,
        new UserDomain(this.userId),
        category
    );
  }
}