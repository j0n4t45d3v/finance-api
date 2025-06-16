package br.com.managementfinanceapi.adapter.in.dto.transaction;


import java.math.BigDecimal;

import br.com.managementfinanceapi.application.core.domain.transaction.BalanceDomain;

public record CreateBalanceDto(
    BigDecimal amount
) {
  public BalanceDomain toDomain() {
    return new BalanceDomain(this.amount(), null);
  }
}