package br.com.managementfinanceapi.application.core.usecase.transaction;

import br.com.managementfinanceapi.application.core.domain.category.CategoryDomain;
import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;
import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.application.port.in.category.SearchCategoryPort;
import br.com.managementfinanceapi.application.port.in.transaction.CreateTransactionPort;
import br.com.managementfinanceapi.application.port.in.user.SearchUserPort;
import br.com.managementfinanceapi.application.port.out.transaction.SaveTransactionRepositoryPort;

public class CreateTransactionUseCase implements CreateTransactionPort {

  private final SaveTransactionRepositoryPort saveTransactionRepositoryPort;
  private final SearchUserPort searchUserPort;
  private final SearchCategoryPort searchCategoryPort;

  public CreateTransactionUseCase(
      SaveTransactionRepositoryPort saveTransactionRepositoryPort,
      SearchUserPort searchUserPort,
      SearchCategoryPort searchCategoryPort
  ) {
    this.saveTransactionRepositoryPort = saveTransactionRepositoryPort;
    this.searchUserPort = searchUserPort;
    this.searchCategoryPort = searchCategoryPort;
  }

  @Override
  public void execute(TransactionDomain transaction, Long userId) {
    UserDomain user = this.searchUserPort.byId(userId);
    transaction.signUser(user);
    CategoryDomain categoryFound = this.searchCategoryPort.byId(userId, transaction.getCategoryId());
    transaction.signCategory(categoryFound);
    this.saveTransactionRepositoryPort.execute(transaction);
  }

}