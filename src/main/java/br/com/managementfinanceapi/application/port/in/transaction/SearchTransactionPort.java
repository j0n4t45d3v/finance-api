package br.com.managementfinanceapi.application.port.in.transaction;

import java.util.List;

import br.com.managementfinanceapi.application.core.domain.common.dvo.DateRange;
import br.com.managementfinanceapi.application.core.domain.common.dvo.Page;
import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;
import br.com.managementfinanceapi.application.core.domain.transaction.dvo.SearchAllFilters;

public interface SearchTransactionPort {
  Page<TransactionDomain> all(SearchAllFilters filters);
  List<TransactionDomain> allByUser(Long userId, DateRange dateRange);
  TransactionDomain byId(Long id);
}