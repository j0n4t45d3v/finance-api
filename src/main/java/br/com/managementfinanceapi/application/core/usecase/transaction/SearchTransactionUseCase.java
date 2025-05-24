package br.com.managementfinanceapi.application.core.usecase.transaction;

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
    if (!filters.isDateRangeValid()) {
      throw new InvalidDateRangeException("Data inicial é menor que a data final!");
    }
    if (filters.isUserIdMissing()) {
      throw new BadRequestException("Usuario não informado!");
    }
    return this.searchRepositoryPort.all(filters);
  }

  @Override
  public TransactionDomain byId(Long id) {
    return this.searchRepositoryPort.byId(id)
              .orElseThrow(() -> new NotFoundException("Transação não encontrada"));
  }
}