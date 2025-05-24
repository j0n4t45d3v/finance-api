package br.com.managementfinanceapi.application.port.in.transaction;

import br.com.managementfinanceapi.application.core.domain.transaction.BalanceDomain;
import br.com.managementfinanceapi.application.core.domain.transaction.dtos.UpdateBalance;

public interface UpdateBalanceOfMonthPort {
  void execute(BalanceDomain balance);
}