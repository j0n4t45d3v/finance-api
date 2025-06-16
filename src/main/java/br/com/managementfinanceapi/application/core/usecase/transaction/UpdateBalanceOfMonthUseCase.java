package br.com.managementfinanceapi.application.core.usecase.transaction;

import java.util.List;
import java.util.Optional;

import br.com.managementfinanceapi.application.core.domain.transaction.BalanceDomain;
import br.com.managementfinanceapi.application.port.in.transaction.UpdateBalanceOfMonthPort;
import br.com.managementfinanceapi.application.port.out.transaction.SaveBalanceRepositoryPort;
import br.com.managementfinanceapi.application.port.out.transaction.SearchBalanceRepositoryPort;

public class UpdateBalanceOfMonthUseCase implements UpdateBalanceOfMonthPort {

  private final SearchBalanceRepositoryPort searchBalanceRepositoryPort;
  private final SaveBalanceRepositoryPort saveBalanceRepositoryPort;

  public UpdateBalanceOfMonthUseCase(
    SearchBalanceRepositoryPort searchBalanceRepositoryPort,
    SaveBalanceRepositoryPort saveBalanceRepositoryPort
  ) {
    this.searchBalanceRepositoryPort = searchBalanceRepositoryPort;
    this.saveBalanceRepositoryPort = saveBalanceRepositoryPort;
  }

  @Override
  public void execute(final BalanceDomain balance) {
    Optional<BalanceDomain> balanceFound = this.searchBalanceRepositoryPort.userBalanceForMonthYear(
        balance.getUser().getId(),
        balance.getMonth(),
        balance.getYear());

    BalanceDomain balanceUpdated = balanceFound.map(b -> {
      b.addAmount(balance.getAmount());
      return b;
    }).orElse(balance);

    this.saveBalanceRepositoryPort.one(balanceUpdated);
    this.readjustSubsequentBalances(balance);
  }

  private void readjustSubsequentBalances(final BalanceDomain balance) {
    List<BalanceDomain> balances = this.searchBalanceRepositoryPort.userBalancesAfterMonthYear(
        balance.getMonth(),
        balance.getYear(),
        balance.getUser().getId());

    for (BalanceDomain currentBalance : balances) {
      currentBalance.addAmount(balance.getAmount());
    }
    this.saveBalanceRepositoryPort.all(balances);
  }
}