package br.com.managementfinanceapi.application.core.domain.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.managementfinanceapi.application.core.domain.category.CategoryDomain;
import br.com.managementfinanceapi.application.core.domain.transaction.enums.TransactionType;
import br.com.managementfinanceapi.application.core.domain.user.UserDomain;


public class TransactionDomain {
  private Long id;
  private BigDecimal amount;
  private TransactionType type;
  private String description;
  private LocalDateTime date;
  private UserDomain user;
  private CategoryDomain category;

  public TransactionDomain(
      Long id,
      BigDecimal value,
      TransactionType type,
      String description,
      LocalDateTime date,
      UserDomain user,
      CategoryDomain category
  ) {
    this.id = id;
    this.amount = value;
    this.type = type;
    this.description = description;
    this.date = date;
    this.user = user;
    this.category = category;
  }

  public TransactionDomain() {
  }

  public Long getId() {
    return id;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public TransactionType getType() {
    return type;
  }

  public String getDescription() {
    return description;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public UserDomain getUser() {
    return user;
  }

  public CategoryDomain getCategory() {
    return category;
  }

  public boolean isExpence() {
    return this.type.equals(TransactionType.EXPENSE);
  }

}