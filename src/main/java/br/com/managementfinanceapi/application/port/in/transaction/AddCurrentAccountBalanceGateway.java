package br.com.managementfinanceapi.application.port.in.transaction;

import br.com.managementfinanceapi.application.core.domain.user.dto.UserBalanceDto;

public interface AddCurrentAccountBalanceGateway {
  void execute(Long userId, UserBalanceDto balance);
}
