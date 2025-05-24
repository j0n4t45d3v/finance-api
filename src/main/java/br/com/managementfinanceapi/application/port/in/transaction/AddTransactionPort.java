package br.com.managementfinanceapi.application.port.in.transaction;

import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;

public interface AddTransactionPort {
  void add(TransactionDomain transaction);
}