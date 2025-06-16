package br.com.managementfinanceapi.application.port.out.transaction;

import java.util.Optional;

import br.com.managementfinanceapi.application.core.domain.common.dvo.Page;
import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;
import br.com.managementfinanceapi.application.core.domain.transaction.dvo.SearchAllFilters;

public interface SearchTransactionRespositoryPort {
  Page<TransactionDomain> all(SearchAllFilters filters);
  Page<TransactionDomain> allByUser(Long userId);
  Optional<TransactionDomain> byId(Long id);
}