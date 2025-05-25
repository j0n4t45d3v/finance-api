package br.com.managementfinanceapi.application.port.in.transaction;

import br.com.managementfinanceapi.application.core.domain.transaction.BalanceDomain;

public interface UpdateBalanceOfMonthPort {
  void execute(BalanceDomain balance);
}