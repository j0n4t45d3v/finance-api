package br.com.managementfinanceapi.application.core.usecase.transaction;

import br.com.managementfinanceapi.application.core.domain.category.Category;
import br.com.managementfinanceapi.application.core.domain.transaction.dtos.UpdateBalance;
import br.com.managementfinanceapi.application.core.domain.transaction.enums.TransactionType;
import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.application.port.in.transaction.AddTransactionGateway;
import br.com.managementfinanceapi.application.core.domain.transaction.Transaction;
import br.com.managementfinanceapi.application.core.domain.transaction.dtos.AddTransaction;
import br.com.managementfinanceapi.application.port.in.transaction.UpdateBalanceOfMonthGateway;
import br.com.managementfinanceapi.adapter.out.repository.transaction.TransactionRepository;
import br.com.managementfinanceapi.application.port.in.user.SearchUserPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class AddTransactionUseCase implements AddTransactionGateway {

  private static final Logger log = LoggerFactory.getLogger(AddTransactionUseCase.class);
  private final TransactionRepository transactionRepository;
  private final SearchUserPort searchUserPort;
  private final UpdateBalanceOfMonthGateway updateBalanceOfMonth;

  public AddTransactionUseCase(
      TransactionRepository transactionRepository,
      SearchUserPort searchUserPort,
      UpdateBalanceOfMonthGateway updateBalanceOfMonth
  ) {
    this.transactionRepository = transactionRepository;
    this.searchUserPort = searchUserPort;
    this.updateBalanceOfMonth = updateBalanceOfMonth;
  }

  @Override
  public void add(AddTransaction body) {
    log.info("(add transaction) transaction received: {}", body);

    UserDomain userDomainResponse = this.searchUserPort.byId(body.userId());
    log.info("(add transaction) user found: {}", userDomainResponse);

    Category category = new Category();
    category.setId(body.categoryId());
    Transaction transaction = new Transaction(body, userDomainResponse, category);
    Transaction transactionCreated = this.transactionRepository.save(transaction);
    log.info("(add transaction) transaction added: {}", transactionCreated);

    this.updateBalanceOfMonth.execute(this.updateBalanceBody(body));
  }

  private UpdateBalance updateBalanceBody(AddTransaction transaction) {
    LocalDate date = transaction.date().toLocalDate();
    short month = (short) date.getMonth().getValue();
    short year = (short) date.getYear();
    BigDecimal amount = transaction.amount();
    if (transaction.type().equals(TransactionType.EXPENSE)) {
      amount = amount.multiply(BigDecimal.valueOf(-1));
    }
    return new UpdateBalance(amount, month, year, transaction.userId());
  }
}