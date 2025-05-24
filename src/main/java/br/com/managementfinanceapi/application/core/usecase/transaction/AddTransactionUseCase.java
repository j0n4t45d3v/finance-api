package br.com.managementfinanceapi.application.core.usecase.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.managementfinanceapi.application.core.domain.transaction.BalanceDomain;
import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;
import br.com.managementfinanceapi.application.port.in.transaction.AddTransactionPort;
import br.com.managementfinanceapi.application.port.in.transaction.UpdateBalanceOfMonthPort;
import br.com.managementfinanceapi.application.port.in.user.SearchUserPort;
import br.com.managementfinanceapi.application.port.out.transaction.SaveTransactionRepositoryPort;

public class AddTransactionUseCase implements AddTransactionPort {

  private final SaveTransactionRepositoryPort saveTransactionRepositoryPort;
  private final SearchUserPort searchUserPort;
  private final UpdateBalanceOfMonthPort updateBalanceOfMonth;

  public AddTransactionUseCase(
      SaveTransactionRepositoryPort saveTransactionRepositoryPort,
      SearchUserPort searchUserPort,
      UpdateBalanceOfMonthPort updateBalanceOfMonth
  ) {
    this.saveTransactionRepositoryPort = saveTransactionRepositoryPort;
    this.searchUserPort = searchUserPort;
    this.updateBalanceOfMonth = updateBalanceOfMonth;
  }

  @Override
  public void add(TransactionDomain transaction) {
    this.searchUserPort.byId(transaction.getUser().getId());
    this.saveTransactionRepositoryPort.execute(transaction);
    this.updateBalanceOfMonth.execute(this.updateBalanceBody(transaction));
  }

  private BalanceDomain updateBalanceBody(TransactionDomain transaction) {
    LocalDate date = transaction.getDate().toLocalDate();
    short month = (short) date.getMonth().getValue();
    short year = (short) date.getYear();
    BigDecimal amount = transaction.getAmount();
    if (transaction.isExpence()) {
      amount = amount.multiply(BigDecimal.valueOf(-1));
    }
    return new BalanceDomain(amount, transaction.getUser(), month, year);
  }
}