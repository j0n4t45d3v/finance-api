package br.com.managementfinanceapi.adapter.out.mapper.transaction;

import org.springframework.stereotype.Component;

import br.com.managementfinanceapi.adapter.out.entity.category.CategoryEntity;
import br.com.managementfinanceapi.adapter.out.entity.transaction.TransactionEntity;
import br.com.managementfinanceapi.adapter.out.entity.user.UserEntity;
import br.com.managementfinanceapi.adapter.out.mapper.Mapper;
import br.com.managementfinanceapi.application.core.domain.category.CategoryDomain;
import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;
import br.com.managementfinanceapi.application.core.domain.user.UserDomain;

@Component
public class TransactionMapper implements Mapper<TransactionEntity, TransactionDomain> {

  private final Mapper<UserEntity, UserDomain> userMapper;
  private final Mapper<CategoryEntity, CategoryDomain> categoryMapper;

  public TransactionMapper(
    Mapper<UserEntity, UserDomain> userMapper,
    Mapper<CategoryEntity, CategoryDomain> categoryMapper
  ) {
    this.userMapper = userMapper;
    this.categoryMapper = categoryMapper;
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
        this.categoryMapper.toEntity(domain.getCategory())
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
        this.categoryMapper.toDomain(entity.getCategory())
    );
  }

}