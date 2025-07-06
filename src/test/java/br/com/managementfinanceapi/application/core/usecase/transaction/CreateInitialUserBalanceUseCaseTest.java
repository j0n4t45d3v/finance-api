package br.com.managementfinanceapi.application.core.usecase.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import br.com.managementfinanceapi.application.core.domain.common.dvo.PageDto;
import br.com.managementfinanceapi.application.core.domain.common.exception.BadRequestException;
import br.com.managementfinanceapi.application.core.domain.transaction.BalanceDomain;
import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;
import br.com.managementfinanceapi.application.port.out.transaction.SaveBalanceRepositoryPort;
import br.com.managementfinanceapi.application.port.out.transaction.SearchBalanceRepositoryPort;
import br.com.managementfinanceapi.application.port.out.transaction.SearchTransactionRespositoryPort;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateInitialUserBalanceUseCaseTest {

  @Mock
  private SearchBalanceRepositoryPort searchBalanceRepository;
  @Mock
  private SaveBalanceRepositoryPort saveBalanceRepository;
  @Mock
  private SearchTransactionRespositoryPort searchTransactionPort;
  @InjectMocks
  private CreateInitialUserBalanceUseCase createInitialUserBalanceUseCase;

  @Test
  @DisplayName("should register initial user balance when user has no balance and has transactions")
  void shouldRegisterInitialUserBalanceWhenUserHasNoBalanceAndHasTransactions() {
    TransactionDomain transactionMock = mock(TransactionDomain.class);

    when(this.searchBalanceRepository.allByUser(anyLong()))
        .thenReturn(List.of());

    when(this.searchTransactionPort.allByUser(anyLong()))
        .thenReturn(new PageDto<>(List.of(transactionMock), 0, 5, 1));

    BalanceDomain initialBalance = new BalanceDomain(
        null,
        BigDecimal.TEN,
        null,
        Short.valueOf("1"),
        Short.valueOf("2025")
    );
    this.createInitialUserBalanceUseCase.execute(1L, initialBalance);

    verify(this.searchBalanceRepository, times(1))
        .allByUser(anyLong());
    verify(this.searchTransactionPort, times(1))
        .allByUser(anyLong());
    verify(this.saveBalanceRepository, times(1))
        .one(initialBalance);
  }

  @Test
  @DisplayName("should throw initial balance already register when a balance already exist for the user")
  void shouldThrowInitialBalanceAlreadyRegisterWhenABalanceAlreadyExistForTheUser() {
    BalanceDomain balanceMock = mock(BalanceDomain.class);
    when(this.searchBalanceRepository.allByUser(anyLong()))
        .thenReturn(List.of(balanceMock));

    BalanceDomain initialBalance = new BalanceDomain(
        null,
        BigDecimal.TEN,
        null,
        Short.valueOf("1"),
        Short.valueOf("2025")
    );
    BadRequestException thrown = assertThrows(
        BadRequestException.class,
        () -> this.createInitialUserBalanceUseCase.execute(1L, initialBalance)
    );
    assertEquals(400, thrown.getCode());
    assertEquals("Usuário já possui um saldo inicial cadastrado", thrown.getMessage());

    verify(this.searchBalanceRepository, times(1))
        .allByUser(anyLong());
    verify(this.searchTransactionPort, times(0))
        .allByUser(anyLong());
    verify(this.saveBalanceRepository, times(0))
        .one(initialBalance);
  }

  @Test
  @DisplayName("should throw has no transaction for this balance when not have transactions throws previous initial balance")
  void shouldThrowHasNoTransactionForThisBalanceWhenNotHaveTransactionsThrowsPreviousInitialBalance() {
    when(this.searchBalanceRepository.allByUser(anyLong()))
        .thenReturn(List.of());

    when(this.searchTransactionPort.allByUser(anyLong()))
        .thenReturn(new PageDto<>(List.of(), 0, 5, 0));

    BalanceDomain initialBalance = new BalanceDomain(
        null,
        BigDecimal.TEN,
        null,
        Short.valueOf("1"),
        Short.valueOf("2025")
    );
    BadRequestException thrown = assertThrows(
        BadRequestException.class,
        () -> this.createInitialUserBalanceUseCase.execute(1L, initialBalance)
    );

    assertEquals(400, thrown.getCode());
    assertEquals("Antes de registrar o seu saldo atual, primeiro cadastre as transações até o momento do saldo", thrown.getMessage());

    verify(this.searchBalanceRepository, times(1))
        .allByUser(anyLong());
    verify(this.searchTransactionPort, times(1))
        .allByUser(anyLong());
    verify(this.saveBalanceRepository, times(0))
        .one(initialBalance);
  }

}
