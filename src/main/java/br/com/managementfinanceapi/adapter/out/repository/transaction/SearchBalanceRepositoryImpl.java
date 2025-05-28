package br.com.managementfinanceapi.adapter.out.repository.transaction;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.managementfinanceapi.adapter.out.entity.transaction.BalanceEntity;
import br.com.managementfinanceapi.adapter.out.mapper.Mapper;
import br.com.managementfinanceapi.application.core.domain.transaction.BalanceDomain;
import br.com.managementfinanceapi.application.port.out.transaction.SearchBalanceRepositoryPort;

@Component
public class SearchBalanceRepositoryImpl implements SearchBalanceRepositoryPort {

  private final BalanceRepository repository;
  private final Mapper<BalanceEntity, BalanceDomain> mapper;

  public SearchBalanceRepositoryImpl(
    BalanceRepository repository,
    Mapper<BalanceEntity, BalanceDomain> mapper
  ) {
    this.mapper = mapper;
    this.repository = repository;
  }

  @Override
  public List<BalanceDomain> allByUser(Long userId) {
    return this.repository.findAllByUserId(userId)
                          .stream()
                          .map(this.mapper::toDomain)
                          .toList();
  }

  @Override
  public Optional<BalanceDomain> userBalanceForMonthYear(Long id, Short month, Short year) {
    return this.repository.findByUserIdAndMonthAndYear(id, month, year)
                          .map(this.mapper::toDomain);
  }

  @Override
  public List<BalanceDomain> userBalancesAfterMonthYear(Short month, Short year, Long id) {
    return this.repository.findAllBalancesAfterMonthYear(month, year, id)
                          .stream()
                          .map(this.mapper::toDomain)
                          .toList();
  }


}