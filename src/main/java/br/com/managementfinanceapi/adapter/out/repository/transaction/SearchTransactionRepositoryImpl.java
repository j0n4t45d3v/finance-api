package br.com.managementfinanceapi.adapter.out.repository.transaction;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import br.com.managementfinanceapi.adapter.out.entity.transaction.TransactionEntity;
import br.com.managementfinanceapi.adapter.out.mapper.Mapper;
import br.com.managementfinanceapi.application.core.domain.common.dvo.DateRange;
import br.com.managementfinanceapi.application.core.domain.common.dvo.Page;
import br.com.managementfinanceapi.application.core.domain.common.dvo.PageFilter;
import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;
import br.com.managementfinanceapi.application.core.domain.transaction.dvo.SearchAllFilters;
import br.com.managementfinanceapi.application.port.out.transaction.SearchTransactionRespositoryPort;

@Component
public class SearchTransactionRepositoryImpl implements SearchTransactionRespositoryPort{
  private final TransactionRepository repository;

  private final Mapper<TransactionEntity, TransactionDomain> mapper;

  public SearchTransactionRepositoryImpl(
    TransactionRepository repository,
    Mapper<TransactionEntity, TransactionDomain> mapper
  ) {
    this.mapper = mapper;
    this.repository = repository;
  }

  @Override
  public Page<TransactionDomain> all(SearchAllFilters filters) {
    DateRange range = filters.dateRange();
    PageFilter page = filters.page();
    Pageable pageable = PageRequest.of(page.page(), page.size());

    org.springframework.data.domain.Page<TransactionDomain> transactions =
      this.repository.findAllByUserIdAndDateBetween(
      filters.userId(),
      range.startWithTime(),
      range.endWithTime(),
      pageable
    ).map(this.mapper::toDomain);

    return new Page<>(
      transactions.getContent(),
      transactions.getNumber(),
      transactions.getSize(),
      transactions.getTotalPages()
    );
  }

  @Override
  public Page<TransactionDomain> allByUser(Long userId) {

    Pageable pageable = PageRequest.of(1, 20);
    org.springframework.data.domain.Page<TransactionDomain> transactions =
      this.repository.findAllByUserId(userId, pageable).map(this.mapper::toDomain);
    return new Page<>(
      transactions.getContent(),
      transactions.getNumber(),
      transactions.getSize(),
      transactions.getTotalPages()
    );
  }

  @Override
  public Optional<TransactionDomain> byId(Long id) {
    return this.repository.findById(id).map(this.mapper::toDomain);
  }

  @Override
  public List<TransactionDomain> allByUser(Long userId, DateRange dateRange) {
    return this.repository
               .findAllByUserIdAndDateBetween(
                 userId,
                 dateRange.startWithTime(),
                 dateRange.endWithTime()
               )
               .stream()
               .map(this.mapper::toDomain)
               .toList();
  }
}