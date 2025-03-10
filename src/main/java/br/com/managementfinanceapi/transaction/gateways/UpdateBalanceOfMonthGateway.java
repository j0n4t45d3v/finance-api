package br.com.managementfinanceapi.transaction.gateways;

import br.com.managementfinanceapi.transaction.domain.dtos.UpdateBalance;

public interface UpdateBalanceOfMonthGateway {
  void execute(UpdateBalance updateBalance);
}
