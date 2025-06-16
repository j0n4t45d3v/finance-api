package br.com.managementfinanceapi.application.port.out.transaction;

import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;

public interface SaveTransactionRepositoryPort {
  TransactionDomain execute(TransactionDomain transaction);
}
