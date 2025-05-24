package br.com.managementfinanceapi.adapter.out.repository.transaction;

import java.util.List;

import br.com.managementfinanceapi.adapter.out.mapper.Mapper;
import br.com.managementfinanceapi.adapter.out.entity.transaction.BalanceEntity;
import br.com.managementfinanceapi.application.core.domain.transaction.BalanceDomain;
import br.com.managementfinanceapi.application.port.out.transaction.SaveBalanceRepositoryPort;

public class SaveBalanceRepositoryImpl implements SaveBalanceRepositoryPort {

  private final BalanceRepository repository;
  private final Mapper<BalanceEntity, BalanceDomain> mapper;

  public SaveBalanceRepositoryImpl(
    BalanceRepository repository,
    Mapper<BalanceEntity, BalanceDomain> mapper
  ) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public BalanceDomain one(BalanceDomain balance) {
    BalanceEntity balanceSaved = this.repository.save(this.mapper.toEntity(balance));
    return this.mapper.toDomain(balanceSaved);
  }

  @Override
  public List<BalanceDomain> all(List<BalanceDomain> balances) {
    List<BalanceEntity> balancesMappedToEntity = balances.stream()
                                                         .map(this.mapper::toEntity)
                                                         .toList();

    return this.repository.saveAll(balancesMappedToEntity)
                        .stream()
                        .map(this.mapper::toDomain)
                        .toList();
  }

}