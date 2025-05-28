package br.com.managementfinanceapi.adapter.in.dto.transaction;

import br.com.managementfinanceapi.application.core.domain.category.Category;
import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;
import br.com.managementfinanceapi.application.core.domain.transaction.enums.TransactionType;
import br.com.managementfinanceapi.application.core.domain.user.UserDomain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AddTransaction(
    BigDecimal amount,
    String description,
    LocalDateTime date,
    TransactionType type,
    Long userId,
    Long categoryId) {
  public TransactionDomain toDomain() {

    CategoryDomain category = new CategoryDomain();
    category.setId(this.categoryId);
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