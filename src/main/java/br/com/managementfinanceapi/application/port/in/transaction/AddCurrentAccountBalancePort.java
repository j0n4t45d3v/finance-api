package br.com.managementfinanceapi.application.port.in.transaction;

import br.com.managementfinanceapi.application.core.domain.transaction.BalanceDomain;

public interface AddCurrentAccountBalancePort {
  void execute(Long userId, BalanceDomain balance);
}