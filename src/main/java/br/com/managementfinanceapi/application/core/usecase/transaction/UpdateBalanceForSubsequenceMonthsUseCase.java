package br.com.managementfinanceapi.application.core.usecase.transaction;

import br.com.managementfinanceapi.application.core.domain.transaction.BalanceDomain;
import br.com.managementfinanceapi.application.port.in.transaction.UpdateBalanceForSubsequenceMonthsPort;
import br.com.managementfinanceapi.application.port.out.transaction.SaveBalanceRepositoryPort;
import br.com.managementfinanceapi.application.port.out.transaction.SearchBalanceRepositoryPort;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

public class UpdateBalanceForSubsequenceMonthsUseCase implements UpdateBalanceForSubsequenceMonthsPort {

  private final SaveBalanceRepositoryPort saveBalanceRepositoryPort;
  private final SearchBalanceRepositoryPort searchBalanceRepositoryPort;

  public UpdateBalanceForSubsequenceMonthsUseCase(
      SaveBalanceRepositoryPort saveBalanceRepositoryPort,
      SearchBalanceRepositoryPort searchBalanceRepositoryPort
  ) {
    this.saveBalanceRepositoryPort = saveBalanceRepositoryPort;
    this.searchBalanceRepositoryPort = searchBalanceRepositoryPort;
  }

  @Override
  public void execute(Long userId, BigDecimal amount, YearMonth yearMonth) {
    List<BalanceDomain> balances = this.getAllUserBalancesAfterMonth(userId, yearMonth);
    List<BalanceDomain> balancesReadjusted = balances
        .stream()
        .map(balance -> {
          balance.addAmount(amount);
          return balance;
        })
        .toList();
    this.saveBalanceRepositoryPort.all(balancesReadjusted);
  }

  private List<BalanceDomain> getAllUserBalancesAfterMonth(Long userId, YearMonth yearMonth) {
    return this.searchBalanceRepositoryPort
        .userBalancesAfterMonthYear((short) yearMonth.getMonthValue(), (short) yearMonth.getYear(), userId);
  }
}
