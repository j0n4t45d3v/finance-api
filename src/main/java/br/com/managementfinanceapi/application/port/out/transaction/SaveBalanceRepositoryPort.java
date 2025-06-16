package br.com.managementfinanceapi.application.port.out.transaction;

import java.util.List;

import br.com.managementfinanceapi.application.core.domain.transaction.BalanceDomain;

public interface SaveBalanceRepositoryPort {
  BalanceDomain one(BalanceDomain balance);
  List<BalanceDomain> all(List<BalanceDomain> balances);
}