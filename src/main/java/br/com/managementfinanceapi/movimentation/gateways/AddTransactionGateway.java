package br.com.managementfinanceapi.movimentation.gateways;

import br.com.managementfinanceapi.movimentation.domain.dtos.AddTransaction;

public interface AddTransactionGateway {
  void add(AddTransaction body);
}
