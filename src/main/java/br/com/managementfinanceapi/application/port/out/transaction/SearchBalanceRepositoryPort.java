package br.com.managementfinanceapi.application.port.out.transaction;

import java.util.List;
import java.util.Optional;

import br.com.managementfinanceapi.application.core.domain.transaction.BalanceDomain;

public interface SearchBalanceRepositoryPort {
  List<BalanceDomain> allByUser(Long userId);
  Optional<BalanceDomain> userBalanceForMonthYear(Long id, Short month, Short year);
  List<BalanceDomain> userBalancesAfterMonthYear(Short month, Short year, Long id);
}