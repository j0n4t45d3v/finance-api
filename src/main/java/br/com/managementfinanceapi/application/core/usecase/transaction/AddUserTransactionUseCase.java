package br.com.managementfinanceapi.application.core.usecase.transaction;

import br.com.managementfinanceapi.application.core.domain.transaction.BalanceDomain;
import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;
import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.application.port.in.transaction.AddUserTransactionPort;
import br.com.managementfinanceapi.application.port.in.transaction.CreateTransactionPort;
import br.com.managementfinanceapi.application.port.in.transaction.UpdateBalanceOfMonthPort;

import java.time.YearMonth;

public class AddUserTransactionUseCase implements AddUserTransactionPort {

  private final CreateTransactionPort createTransactionPort;
  private final UpdateBalanceOfMonthPort updateBalanceOfMonthPort;
  private final UpdateBalanceForSubsequenceMonthsUseCase updateBalanceForSubsequenceMonthsUseCase;

  public AddUserTransactionUseCase(
      CreateTransactionPort createTransactionPort,
      UpdateBalanceOfMonthPort updateBalanceOfMonthPort,
      UpdateBalanceForSubsequenceMonthsUseCase updateBalanceForSubsequenceMonthsUseCase
  ) {
    this.createTransactionPort = createTransactionPort;
    this.updateBalanceOfMonthPort = updateBalanceOfMonthPort;
    this.updateBalanceForSubsequenceMonthsUseCase = updateBalanceForSubsequenceMonthsUseCase;
  }

  @Override
  public void execute(TransactionDomain newTransaction, Long userId) {
    this.createTransactionPort.execute(newTransaction, userId);
    this.updateBalance(newTransaction, userId);
  }

  private void updateBalance(TransactionDomain newTransaction, Long userId){
    BalanceDomain balance = this.createBalance(newTransaction, userId);
    YearMonth yearMonth = YearMonth.of(balance.getYear(), balance.getMonth());
    this.updateBalanceOfMonthPort.execute(balance);
    this.updateBalanceForSubsequenceMonthsUseCase.execute(userId, balance.getAmount(), yearMonth);
  }

  private BalanceDomain createBalance(TransactionDomain newTransaction, Long userId){
    short month = (short) newTransaction.getDate().getMonthValue();
    short year = (short) newTransaction.getDate().getYear();
    return new BalanceDomain(newTransaction.getAmount(), new UserDomain(userId), month, year);
  }

}
