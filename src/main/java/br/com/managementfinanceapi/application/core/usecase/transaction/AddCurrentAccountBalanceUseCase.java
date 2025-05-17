package br.com.managementfinanceapi.application.core.usecase.transaction;

import br.com.managementfinanceapi.infra.error.exceptions.BadRequestException;
import br.com.managementfinanceapi.adapter.out.repository.transaction.TransactionRepository;
import br.com.managementfinanceapi.application.core.domain.transaction.Balance;
import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.application.core.domain.user.dto.UserBalanceDto;
import br.com.managementfinanceapi.application.port.in.transaction.AddCurrentAccountBalanceGateway;
import br.com.managementfinanceapi.adapter.out.repository.transaction.BalanceRepository;
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
    UserDomain userDomain = new UserDomain();
    userDomain.setId(userId);
    Balance newBalance = new Balance(null, balance.amount(), userDomain, month, year);
    this.repository.save(newBalance);
  }
}
