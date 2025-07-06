package br.com.managementfinanceapi.application.core.usecase.transaction;

import java.util.List;

import br.com.managementfinanceapi.application.core.domain.common.dvo.PageDto;
import br.com.managementfinanceapi.application.core.domain.common.exception.BadRequestException;
import br.com.managementfinanceapi.application.core.domain.transaction.BalanceDomain;
import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;
import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.application.port.in.transaction.CreateInitialUserBalancePort;
import br.com.managementfinanceapi.application.port.out.transaction.SaveBalanceRepositoryPort;
import br.com.managementfinanceapi.application.port.out.transaction.SearchBalanceRepositoryPort;
import br.com.managementfinanceapi.application.port.out.transaction.SearchTransactionRespositoryPort;

public class CreateInitialUserBalanceUseCase implements CreateInitialUserBalancePort {

  private final SearchBalanceRepositoryPort searchBalanceRepository;
  private final SaveBalanceRepositoryPort saveBalanceRepository;
  private final SearchTransactionRespositoryPort searchTransactionPort;

  public CreateInitialUserBalanceUseCase(
      SearchBalanceRepositoryPort searchBalanceRepository,
      SaveBalanceRepositoryPort saveBalanceRepository,
      SearchTransactionRespositoryPort searchTransactionPort
  ) {
    this.searchBalanceRepository = searchBalanceRepository;
    this.saveBalanceRepository = saveBalanceRepository;
    this.searchTransactionPort = searchTransactionPort;
  }

  @Override
  public void execute(Long userId, BalanceDomain balance) {
    List<BalanceDomain> balances = this.searchBalanceRepository.allByUser(userId);
    if (!balances.isEmpty()) {
      throw new BadRequestException("Usuário já possui um saldo inicial cadastrado");
    }

    PageDto<TransactionDomain> transaction = this.searchTransactionPort.allByUser(userId);
    if (transaction.isEmptyContent()) {
      throw new BadRequestException("Antes de registrar o seu saldo atual, primeiro cadastre as transações até o momento do saldo");
    }
    balance.signUser(new UserDomain(userId));
    this.saveBalanceRepository.one(balance);
  }

}