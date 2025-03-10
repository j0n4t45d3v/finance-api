package br.com.managementfinanceapi.transaction.usecase;

import br.com.managementfinanceapi.infra.error.exceptions.BadRequestException;
import br.com.managementfinanceapi.transaction.repository.TransactionRepository;
import br.com.managementfinanceapi.transaction.domain.Balance;
import br.com.managementfinanceapi.user.domain.User;
import br.com.managementfinanceapi.user.domain.dto.UserBalanceDto;
import br.com.managementfinanceapi.transaction.gateways.AddCurrentAccountBalanceGateway;
import br.com.managementfinanceapi.transaction.repository.BalanceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AddCurrentAccountBalanceUseCase implements AddCurrentAccountBalanceGateway {

  private final BalanceRepository repository;
  private final TransactionRepository transactionRepository;

  public AddCurrentAccountBalanceUseCase(BalanceRepository repository, TransactionRepository transactionRepository) {
    this.repository = repository;
    this.transactionRepository = transactionRepository;
  }

  @Override
  public void execute(Long userId, UserBalanceDto balance) {
    var balances = this.repository.findAllByUserId(userId);
    if(balances.findAny().isPresent()) {
      throw new BadRequestException("Usuario já possui saldo registrado");
    }
    var transaction = this.transactionRepository.findAllByUserId(userId);

    if(transaction.findAny().isEmpty()) {
      throw new BadRequestException("Antes de registrar o seu saldo atual, resgistre primeiro as transações até o momento do saldo");
    }

    LocalDate dateNow = LocalDate.now();
    short month = (short) dateNow.getMonthValue();
    short year = (short) dateNow.getYear();
    User user = new User();
    user.setId(userId);
    Balance newBalance = new Balance(null, balance.amount(), user, month, year);
    this.repository.save(newBalance);
  }
}
