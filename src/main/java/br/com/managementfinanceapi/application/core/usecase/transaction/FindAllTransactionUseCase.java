package br.com.managementfinanceapi.application.core.usecase.transaction;

import br.com.managementfinanceapi.application.core.domain.transaction.dtos.TransactionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.managementfinanceapi.application.core.domain.transaction.dtos.SearchTransaction;
import br.com.managementfinanceapi.application.port.in.transaction.FindAllTransactionGateway;
import br.com.managementfinanceapi.adapter.out.repository.transaction.TransactionRepository;

@Service
public class FindAllTransactionUseCase implements FindAllTransactionGateway {

  private static final Logger log = LoggerFactory.getLogger(FindAllTransactionUseCase.class);
  private final TransactionRepository transactionRepository;

  public FindAllTransactionUseCase(TransactionRepository transactionRepository) {
    this.transactionRepository = transactionRepository;
  }

  @Override
  public Page<TransactionDto> findAll(SearchTransaction filters, Pageable page) {
    return this.transactionRepository.findAllByUserIdAndDateBetween(
        filters.userId(),
        filters.startDate().atTime(0, 0),
        filters.endDate().atTime(23, 59),
        page
    ).map(TransactionDto::new);
  }

}