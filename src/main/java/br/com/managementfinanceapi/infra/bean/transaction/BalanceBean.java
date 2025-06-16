package br.com.managementfinanceapi.infra.bean.transaction;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import br.com.managementfinanceapi.application.core.usecase.transaction.AddCurrentAccountBalanceUseCase;
import br.com.managementfinanceapi.application.core.usecase.transaction.UpdateBalanceOfMonthUseCase;
import br.com.managementfinanceapi.application.port.in.transaction.AddCurrentAccountBalancePort;
import br.com.managementfinanceapi.application.port.in.transaction.UpdateBalanceOfMonthPort;
import br.com.managementfinanceapi.application.port.out.transaction.SaveBalanceRepositoryPort;
import br.com.managementfinanceapi.application.port.out.transaction.SearchBalanceRepositoryPort;
import br.com.managementfinanceapi.application.port.out.transaction.SearchTransactionRespositoryPort;

@Component
public class BalanceBean {

  @Bean
  public AddCurrentAccountBalancePort addCurrentAccountBalance(
      SearchBalanceRepositoryPort searchBalanceRepository,
      SaveBalanceRepositoryPort saveBalanceRepository,
      SearchTransactionRespositoryPort searchTransactionPort
  ) {
    return new AddCurrentAccountBalanceUseCase(searchBalanceRepository, saveBalanceRepository, searchTransactionPort);
  }

  @Bean
  public UpdateBalanceOfMonthPort updateBalanceOfMonth(
    SearchBalanceRepositoryPort searchBalanceRepositoryPort,
    SaveBalanceRepositoryPort saveBalanceRepositoryPort
  ){
    return new UpdateBalanceOfMonthUseCase(searchBalanceRepositoryPort, saveBalanceRepositoryPort);
  }

}