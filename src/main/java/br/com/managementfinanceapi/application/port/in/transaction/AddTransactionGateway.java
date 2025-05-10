package br.com.managementfinanceapi.application.port.in.transaction;

import br.com.managementfinanceapi.application.core.domain.transaction.dtos.AddTransaction;

public interface AddTransactionGateway {
  void add(AddTransaction body);
}
