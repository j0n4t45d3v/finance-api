package br.com.managementfinanceapi.application.core.domain.category.dto;

import java.math.BigDecimal;

public class TotalByCategoryView {
  private final String category;
  private final BigDecimal value;
  private final BigDecimal creditLimit;
  private final boolean isNearLimit;

  public TotalByCategoryView(String category, BigDecimal value, BigDecimal creditLimit) {
    this.category = category;
    this.value = value;
    this.creditLimit = creditLimit;
    this.isNearLimit = this.nearLimit();
  }

  private boolean nearLimit() {
    BigDecimal warningValue = this.creditLimit.multiply(BigDecimal.valueOf(0.9));
    return this.value.doubleValue() >= warningValue.doubleValue();
  }

  public String getCategory() {
    return category;
  }

  public BigDecimal getValue() {
    return value;
  }

  public BigDecimal getCreditLimit() {
    return creditLimit;
  }

  public boolean isNearLimit() {
    return isNearLimit;
  }
}
