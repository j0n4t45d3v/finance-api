package br.com.managementfinanceapi.movimentation.gateways;

import br.com.managementfinanceapi.movimentation.domain.Transaction;
import br.com.managementfinanceapi.movimentation.domain.dtos.TransactionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.managementfinanceapi.movimentation.domain.dtos.SearchMovement;

public interface FindAllMovementGateway {
  Page<TransactionDto> findAll(SearchMovement filters, Pageable page);
}