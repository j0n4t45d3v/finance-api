package br.com.managementfinanceapi.application.core.usecase.transaction;

import br.com.managementfinanceapi.application.core.domain.common.dvo.DateRange;
import br.com.managementfinanceapi.application.core.domain.common.dvo.PageDto;
import br.com.managementfinanceapi.application.core.domain.common.exception.InvalidDateRangeException;
import br.com.managementfinanceapi.application.core.domain.common.exception.NotFoundException;
import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;
import br.com.managementfinanceapi.application.core.domain.transaction.dvo.SearchAllFilters;
import br.com.managementfinanceapi.application.port.in.transaction.SearchTransactionPort;
import br.com.managementfinanceapi.application.port.out.transaction.SearchTransactionRespositoryPort;

import java.util.List;

public class SearchTransactionUseCase implements SearchTransactionPort {

  private final SearchTransactionRespositoryPort searchRepositoryPort;

  public SearchTransactionUseCase(SearchTransactionRespositoryPort searchRepositoryPort) {
    this.searchRepositoryPort = searchRepositoryPort;
  }

  @Override
  public PageDto<TransactionDomain> all(SearchAllFilters filters) {
    filters.validate();
    return this.searchRepositoryPort.all(filters);
  }

  @Override
  public List<TransactionDomain> allByUser(Long userId, DateRange dateRange) {
    if (dateRange == null) {
      throw new InvalidDateRangeException("Periodo de data não informado!");
    }
    if (dateRange.isInvalidRange()) {
      throw new InvalidDateRangeException("Data inicial é menor que a data final!");
    }
    return this.searchRepositoryPort.allByUser(userId, dateRange);
  }

  @Override
  public TransactionDomain byId(Long id) {
    return this.searchRepositoryPort.byId(id)
              .orElseThrow(() -> new NotFoundException("Transação não encontrada"));
  }
}