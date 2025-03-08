package br.com.managementfinanceapi.transaction.gateways;

import br.com.managementfinanceapi.transaction.domain.dtos.TransactionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.managementfinanceapi.transaction.domain.dtos.SearchTransaction;

public interface FindAllTransactionGateway {
  Page<TransactionDto> findAll(SearchTransaction filters, Pageable page);
}