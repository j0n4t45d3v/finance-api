package br.com.managementfinanceapi.application.port.out.transaction;

import java.util.List;
import java.util.Optional;

import br.com.managementfinanceapi.application.core.domain.common.dvo.DateRange;
import br.com.managementfinanceapi.application.core.domain.common.dvo.PageDto;
import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;
import br.com.managementfinanceapi.application.core.domain.transaction.dvo.SearchAllFilters;

public interface SearchTransactionRespositoryPort {
  PageDto<TransactionDomain> all(SearchAllFilters filters);
  PageDto<TransactionDomain> allByUser(Long userId);
  List<TransactionDomain> allByUser(Long userId, DateRange dateRange);
  Optional<TransactionDomain> byId(Long id);
}