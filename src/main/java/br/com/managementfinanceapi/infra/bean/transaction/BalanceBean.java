package br.com.managementfinanceapi.infra.bean.transaction;

import br.com.managementfinanceapi.application.core.usecase.transaction.UpdateBalanceForSubsequenceMonthsUseCase;
import br.com.managementfinanceapi.application.port.in.transaction.UpdateBalanceForSubsequenceMonthsPort;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import br.com.managementfinanceapi.application.core.usecase.transaction.CreateInitialUserBalanceUseCase;
import br.com.managementfinanceapi.application.core.usecase.transaction.UpdateBalanceOfMonthUseCase;
import br.com.managementfinanceapi.application.port.in.transaction.CreateInitialUserBalancePort;
import br.com.managementfinanceapi.application.port.in.transaction.UpdateBalanceOfMonthPort;
import br.com.managementfinanceapi.application.port.out.transaction.SaveBalanceRepositoryPort;
import br.com.managementfinanceapi.application.port.out.transaction.SearchBalanceRepositoryPort;
import br.com.managementfinanceapi.application.port.out.transaction.SearchTransactionRespositoryPort;

@Component
public class BalanceBean {

  @Bean
  public CreateInitialUserBalancePort createInitialUserBalance(
      SearchBalanceRepositoryPort searchBalanceRepository,
      SaveBalanceRepositoryPort saveBalanceRepository,
      SearchTransactionRespositoryPort searchTransactionPort
  ) {
    return new CreateInitialUserBalanceUseCase(searchBalanceRepository, saveBalanceRepository, searchTransactionPort);
  }

  @Bean
  public UpdateBalanceOfMonthPort updateBalanceOfMonth(
    SearchBalanceRepositoryPort searchBalanceRepositoryPort,
    SaveBalanceRepositoryPort saveBalanceRepositoryPort
  ){
    return new UpdateBalanceOfMonthUseCase(searchBalanceRepositoryPort, saveBalanceRepositoryPort);
  }


  @Bean
  public UpdateBalanceForSubsequenceMonthsPort updateBalanceForSubsequenceMonths(
      SaveBalanceRepositoryPort saveBalanceRepositoryPort,
      SearchBalanceRepositoryPort searchBalanceRepositoryPort
  ){
    return new UpdateBalanceForSubsequenceMonthsUseCase(saveBalanceRepositoryPort, searchBalanceRepositoryPort);
  }

}