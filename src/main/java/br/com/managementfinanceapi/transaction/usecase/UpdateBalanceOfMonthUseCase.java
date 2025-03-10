package br.com.managementfinanceapi.transaction.usecase;

import br.com.managementfinanceapi.transaction.domain.Balance;
import br.com.managementfinanceapi.transaction.domain.dtos.UpdateBalance;
import br.com.managementfinanceapi.transaction.gateways.UpdateBalanceOfMonthGateway;
import br.com.managementfinanceapi.transaction.repository.BalanceRepository;
import br.com.managementfinanceapi.user.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UpdateBalanceOfMonthUseCase implements UpdateBalanceOfMonthGateway {

  private static final Logger log = LoggerFactory.getLogger(UpdateBalanceOfMonthUseCase.class);
  private final BalanceRepository repository;

  public UpdateBalanceOfMonthUseCase(BalanceRepository repository) {
    this.repository = repository;
  }

  @Override
  public void execute(final UpdateBalance updateBalance) {
    Optional<Balance> balanceFound = this.repository.findByUserIdAndMonthAndYear(
        updateBalance.userId(),
        updateBalance.month(),
        updateBalance.year()
    );
    log.info("(update balance) exists balance in month: {}", balanceFound.isPresent());

    Balance balance = balanceFound.map(b -> {
      b.addAmount(updateBalance.amount());
      log.info("(update balance) add amount {} in balance {}", updateBalance.amount(), b.getId());
      return b;
    }).orElseGet(() -> this.defaultBalance(updateBalance));

    Balance balanceCreatedOrUpdated = this.repository.save(balance);
    log.info("(update balance) created/updated balance: {}", balanceCreatedOrUpdated);

    this.readjustSubsequentBalances(updateBalance);
  }

  private Balance defaultBalance(final UpdateBalance updateBalance) {
    return new Balance(
        updateBalance.amount(),
        new User(updateBalance.userId()),
        updateBalance.month(),
        updateBalance.year()
    );
  }

  private void readjustSubsequentBalances(final UpdateBalance updateBalance) {
    List<Balance> balances = this.repository.findAllBalancesWithMonthANdYearGreater(
        updateBalance.month(),
        updateBalance.year(),
        updateBalance.userId()
    );
    log.info("(update balance) exists subsequent balances: {}", !balances.isEmpty());

    for (Balance currentBalance : balances) {
      currentBalance.addAmount(updateBalance.amount());
    }
    this.repository.saveAll(balances);
  }
}
