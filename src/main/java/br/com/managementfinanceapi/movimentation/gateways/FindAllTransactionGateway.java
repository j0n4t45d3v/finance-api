package br.com.managementfinanceapi.movimentation.gateways;

import br.com.managementfinanceapi.movimentation.domain.dtos.TransactionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.managementfinanceapi.movimentation.domain.dtos.SearchTransaction;

public interface FindAllTransactionGateway {
  Page<TransactionDto> findAll(SearchTransaction filters, Pageable page);
}