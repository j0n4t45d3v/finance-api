package br.com.managementfinanceapi.infra.bean.transaction;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import br.com.managementfinanceapi.application.core.usecase.transaction.AddTransactionUseCase;
import br.com.managementfinanceapi.application.core.usecase.transaction.SearchTransactionUseCase;
import br.com.managementfinanceapi.application.port.in.transaction.AddTransactionPort;
import br.com.managementfinanceapi.application.port.in.transaction.SearchTransactionPort;
import br.com.managementfinanceapi.application.port.in.transaction.UpdateBalanceOfMonthPort;
import br.com.managementfinanceapi.application.port.in.user.SearchUserPort;
import br.com.managementfinanceapi.application.port.out.transaction.SaveTransactionRepositoryPort;
import br.com.managementfinanceapi.application.port.out.transaction.SearchTransactionRespositoryPort;

@Component
public class TransactionBean {

  @Bean
  public AddTransactionPort addTransaction(
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

}