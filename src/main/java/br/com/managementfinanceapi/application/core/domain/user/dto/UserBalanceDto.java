package br.com.managementfinanceapi.application.core.domain.user.dto;


import java.math.BigDecimal;

import br.com.managementfinanceapi.application.core.domain.transaction.BalanceDomain;

public record UserBalanceDto(
    BigDecimal amount
) {
  public BalanceDomain toDomain() {
    return new BalanceDomain(this.amount(), null);
  }
}