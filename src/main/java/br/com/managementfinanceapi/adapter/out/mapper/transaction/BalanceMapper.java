package br.com.managementfinanceapi.adapter.out.mapper.transaction;

import org.springframework.stereotype.Component;

import br.com.managementfinanceapi.adapter.out.mapper.Mapper;
import br.com.managementfinanceapi.adapter.out.entity.transaction.BalanceEntity;
import br.com.managementfinanceapi.adapter.out.entity.user.UserEntity;
import br.com.managementfinanceapi.application.core.domain.transaction.BalanceDomain;
import br.com.managementfinanceapi.application.core.domain.user.UserDomain;

@Component
public class BalanceMapper implements Mapper<BalanceEntity, BalanceDomain> {

  private final Mapper<UserEntity, UserDomain> userMapper;

  public BalanceMapper(Mapper<UserEntity, UserDomain> userMapper) {
    this.userMapper = userMapper;
  }

  @Override
  public BalanceEntity toEntity(BalanceDomain domain) {
    return new BalanceEntity(
      domain.getId(),
      domain.getAmount(),
      this.userMapper.toEntity(domain.getUser()),
      domain.getMonth(),
      domain.getYear()
    );
  }

  @Override
  public BalanceDomain toDomain(BalanceEntity entity) {
    return new BalanceDomain(
      entity.getAmount(),
      this.userMapper.toDomain(entity.getUser()),
      entity.getMonth(),
      entity.getYear()
    );
  }

  
}