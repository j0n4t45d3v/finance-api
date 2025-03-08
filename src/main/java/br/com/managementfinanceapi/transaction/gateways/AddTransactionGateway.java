package br.com.managementfinanceapi.transaction.gateways;

import br.com.managementfinanceapi.transaction.domain.dtos.AddTransaction;

public interface AddTransactionGateway {
  void add(AddTransaction body);
}
