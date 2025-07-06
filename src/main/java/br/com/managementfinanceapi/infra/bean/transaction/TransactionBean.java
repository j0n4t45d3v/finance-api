package br.com.managementfinanceapi.infra.bean.transaction;

import br.com.managementfinanceapi.application.core.usecase.transaction.AddUserTransactionUseCase;
import br.com.managementfinanceapi.application.core.usecase.transaction.CreateTransactionUseCase;
import br.com.managementfinanceapi.application.core.usecase.transaction.SearchTransactionUseCase;
import br.com.managementfinanceapi.application.port.in.category.SearchCategoryPort;
import br.com.managementfinanceapi.application.port.in.transaction.*;
import br.com.managementfinanceapi.application.port.in.user.SearchUserPort;
import br.com.managementfinanceapi.application.port.out.transaction.SaveTransactionRepositoryPort;
import br.com.managementfinanceapi.application.port.out.transaction.SearchTransactionRespositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class TransactionBean {

  @Bean
  public CreateTransactionPort createTransaction(
      SaveTransactionRepositoryPort saveTransactionRepositoryPort,
      SearchUserPort searchUserPort, 
      SearchCategoryPort searchCategoryPort
  ) {
    return new CreateTransactionUseCase(saveTransactionRepositoryPort, searchUserPort, searchCategoryPort);
  }

  @Bean
  public SearchTransactionPort searchTransaction(
    SearchTransactionRespositoryPort searchTransactionRespositoryPort 
  ) {
    return new SearchTransactionUseCase(searchTransactionRespositoryPort);
  }

  @Bean
  public AddUserTransactionPort addUserTransaction(
      CreateTransactionPort createTransactionPort,
      UpdateBalanceOfMonthPort updateBalanceOfMonthPort,
      UpdateBalanceForSubsequenceMonthsPort updateBalanceForSubsequenceMonthsPort
  ){
    return new AddUserTransactionUseCase(
        createTransactionPort,
        updateBalanceOfMonthPort,
        updateBalanceForSubsequenceMonthsPort
    );
  }

}