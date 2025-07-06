package br.com.managementfinanceapi.adapter.out.repository.transaction.implementation;

import br.com.managementfinanceapi.adapter.out.repository.transaction.TransactionRepository;
import org.springframework.stereotype.Component;

import br.com.managementfinanceapi.adapter.out.entity.transaction.TransactionEntity;
import br.com.managementfinanceapi.adapter.out.mapper.Mapper;
import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;
import br.com.managementfinanceapi.application.port.out.transaction.SaveTransactionRepositoryPort;

@Component
public class SaveTransactionRepositoryImpl implements SaveTransactionRepositoryPort {

  private final TransactionRepository repository;
  private final Mapper<TransactionEntity, TransactionDomain> mapper;

  public SaveTransactionRepositoryImpl(
    TransactionRepository repository,
    Mapper<TransactionEntity, TransactionDomain> mapper
  ) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public TransactionDomain execute(TransactionDomain transaction) {
    TransactionEntity transactionSaved = 
      this.repository.save(this.mapper.toEntity(transaction));
    return this.mapper.toDomain(transactionSaved);
  }

}