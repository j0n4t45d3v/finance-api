package br.com.managementfinanceapi.infra.bean.transaction;

import org.springframework.context.annotation.Bean;

import br.com.managementfinanceapi.application.core.usecase.transaction.AddCurrentAccountBalanceUseCase;
import br.com.managementfinanceapi.application.core.usecase.transaction.AddTransactionUseCase;
import br.com.managementfinanceapi.application.core.usecase.transaction.SearchTransactionUseCase;
import br.com.managementfinanceapi.application.core.usecase.transaction.UpdateBalanceOfMonthUseCase;
import br.com.managementfinanceapi.application.port.in.transaction.AddCurrentAccountBalancePort;
import br.com.managementfinanceapi.application.port.in.transaction.AddTransactionPort;
import br.com.managementfinanceapi.application.port.in.transaction.SearchTransactionPort;
import br.com.managementfinanceapi.application.port.in.transaction.UpdateBalanceOfMonthPort;
import br.com.managementfinanceapi.application.port.in.user.SearchUserPort;
import br.com.managementfinanceapi.application.port.out.transaction.SaveBalanceRepositoryPort;
import br.com.managementfinanceapi.application.port.out.transaction.SaveTransactionRepositoryPort;
import br.com.managementfinanceapi.application.port.out.transaction.SearchBalanceRepositoryPort;
import br.com.managementfinanceapi.application.port.out.transaction.SearchTransactionRespositoryPort;

public class TransactionBean {

  @Bean
  public AddCurrentAccountBalancePort addCurrentAccountBalance(
      SearchBalanceRepositoryPort searchBalanceRepository,
      SaveBalanceRepositoryPort saveBalanceRepository,
      SearchTransactionRespositoryPort searchTransactionPort
  ) {
    return new AddCurrentAccountBalanceUseCase(
      searchBalanceRepository,
      saveBalanceRepository,
      searchTransactionPort
    );
  }

  @Bean
  public AddTransactionPort adTransaction(
      SaveTransactionRepositoryPort saveTransactionRepositoryPort,
      SearchUserPort searchUserPort,
      UpdateBalanceOfMonthPort updateBalanceOfMonth
  ) {
    return new AddTransactionUseCase(
      saveTransactionRepositoryPort,
      searchUserPort,
      updateBalanceOfMonth
    );
  }

  @Bean
  public SearchTransactionPort searchTransaction(
    SearchTransactionRespositoryPort searchTransactionRespositoryPort 
  ) {
    return new SearchTransactionUseCase(searchTransactionRespositoryPort);
  }

  @Bean
  public UpdateBalanceOfMonthPort updateBalanceOfMonth(
    SearchBalanceRepositoryPort searchBalanceRepositoryPort,
    SaveBalanceRepositoryPort saveBalanceRepositoryPort
  ) {
    return new UpdateBalanceOfMonthUseCase(
      searchBalanceRepositoryPort,
      saveBalanceRepositoryPort
    );
  }
}