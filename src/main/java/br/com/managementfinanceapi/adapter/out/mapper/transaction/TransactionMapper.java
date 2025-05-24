package br.com.managementfinanceapi.adapter.out.mapper.transaction;

import org.springframework.stereotype.Component;

import br.com.managementfinanceapi.adapter.out.entity.transaction.TransactionEntity;
import br.com.managementfinanceapi.adapter.out.entity.user.UserEntity;
import br.com.managementfinanceapi.adapter.out.mapper.Mapper;
import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;
import br.com.managementfinanceapi.application.core.domain.user.UserDomain;

@Component
public class TransactionMapper implements Mapper<TransactionEntity, TransactionDomain> {

  private final Mapper<UserEntity, UserDomain> userMapper;

  public TransactionMapper(Mapper<UserEntity, UserDomain> userMapper) {
    this.userMapper = userMapper;
  }

  @Override
  public TransactionEntity toEntity(TransactionDomain domain) {
    return new TransactionEntity(
        domain.getId(),
        domain.getAmount(),
        domain.getType(),
        domain.getDescription(),
        domain.getDate(),
        this.userMapper.toEntity(domain.getUser()),
        domain.getCategory()
    );
  }

  @Override
  public TransactionDomain toDomain(TransactionEntity entity) {
    return new TransactionDomain(
        entity.getId(),
        entity.getAmount(),
        entity.getType(),
        entity.getDescription(),
        entity.getDate(),
        this.userMapper.toDomain(entity.getUser()),
        entity.getCategory()
    );
  }

}