package br.com.managementfinanceapi.application.port.out.transaction;

import java.util.List;
import java.util.Optional;

import br.com.managementfinanceapi.application.core.domain.common.dvo.DateRange;
import br.com.managementfinanceapi.application.core.domain.common.dvo.Page;
import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;
import br.com.managementfinanceapi.application.core.domain.transaction.dvo.SearchAllFilters;

public interface SearchTransactionRespositoryPort {
  Page<TransactionDomain> all(SearchAllFilters filters);
  Page<TransactionDomain> allByUser(Long userId);
  List<TransactionDomain> allByUser(Long userId, DateRange dateRange);
  Optional<TransactionDomain> byId(Long id);
}