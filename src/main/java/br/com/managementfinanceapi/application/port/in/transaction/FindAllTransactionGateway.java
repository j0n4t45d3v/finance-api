package br.com.managementfinanceapi.application.port.in.transaction;

import br.com.managementfinanceapi.application.core.domain.transaction.dtos.TransactionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.managementfinanceapi.application.core.domain.transaction.dtos.SearchTransaction;

public interface FindAllTransactionGateway {
  Page<TransactionDto> findAll(SearchTransaction filters, Pageable page);
}