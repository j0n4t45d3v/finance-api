package br.com.managementfinanceapi.application.core.domain.category.dvo;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record CategoryTransactionSummary(
    String name,
    BigDecimal creditLimit,
    BigDecimal income,
    BigDecimal expence,
    BigDecimal totalIncome,
    BigDecimal totalExpence
) {
  public boolean overflowCategoryCreditLimite() {
    return this.creditLimit().compareTo(this.expence()) > 0;
  }

  public BigDecimal incomePercentage() {
    return this.calcPercentage(this.totalIncome(), this.income());
  }

  public BigDecimal expencePercentage() {
    return this.calcPercentage(this.totalExpence(), this.expence());
  }

  private BigDecimal calcPercentage(BigDecimal total, BigDecimal value) {
    return value.divide(total, 4, RoundingMode.HALF_UP);
  }

}