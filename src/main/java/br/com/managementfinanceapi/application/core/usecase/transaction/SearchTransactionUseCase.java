package br.com.managementfinanceapi.application.core.usecase.transaction;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.managementfinanceapi.application.core.domain.common.dvo.DateRange;
import br.com.managementfinanceapi.application.core.domain.common.dvo.Page;
import br.com.managementfinanceapi.application.core.domain.common.exception.BadRequestException;
import br.com.managementfinanceapi.application.core.domain.common.exception.InvalidDateRangeException;
import br.com.managementfinanceapi.application.core.domain.common.exception.NotFoundException;
import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;
import br.com.managementfinanceapi.application.core.domain.transaction.dvo.SearchAllFilters;
import br.com.managementfinanceapi.application.port.in.transaction.SearchTransactionPort;
import br.com.managementfinanceapi.application.port.out.transaction.SearchTransactionRespositoryPort;

public class SearchTransactionUseCase implements SearchTransactionPort {

  private final SearchTransactionRespositoryPort searchRepositoryPort;

  public SearchTransactionUseCase(SearchTransactionRespositoryPort searchRepositoryPort) {
    this.searchRepositoryPort = searchRepositoryPort;
  }

  @Override
  public Page<TransactionDomain> all(SearchAllFilters filters) {
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