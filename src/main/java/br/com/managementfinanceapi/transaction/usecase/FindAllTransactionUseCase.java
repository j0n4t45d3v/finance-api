package br.com.managementfinanceapi.transaction.usecase;

import br.com.managementfinanceapi.transaction.domain.dtos.TransactionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.managementfinanceapi.transaction.domain.dtos.SearchTransaction;
import br.com.managementfinanceapi.transaction.gateways.FindAllTransactionGateway;
import br.com.managementfinanceapi.transaction.repository.TransactionRepository;

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