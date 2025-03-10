package br.com.managementfinanceapi.transaction.gateways;

import br.com.managementfinanceapi.user.domain.dto.UserBalanceDto;

public interface AddCurrentAccountBalanceGateway {
  void execute(Long userId, UserBalanceDto balance);
}
