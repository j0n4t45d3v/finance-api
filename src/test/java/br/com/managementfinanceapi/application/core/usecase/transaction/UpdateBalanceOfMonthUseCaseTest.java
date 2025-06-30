package br.com.managementfinanceapi.application.core.usecase.transaction;

import br.com.managementfinanceapi.application.core.domain.transaction.BalanceDomain;
import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.application.port.out.transaction.SaveBalanceRepositoryPort;
import br.com.managementfinanceapi.application.port.out.transaction.SearchBalanceRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyShort;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateBalanceOfMonthUseCaseTest {

  @Mock
  private SearchBalanceRepositoryPort searchBalanceRepositoryPort;
  @Mock
  private SaveBalanceRepositoryPort saveBalanceRepositoryPort;
  @InjectMocks
  private UpdateBalanceOfMonthUseCase updateBalanceOfMonthUseCase;

  @Test
  @DisplayName("should update balance of year month when balance of month already exists")
  void shouldUpdateBalanceOfYearMonthWhenBalanceOfMonthAlreadyExists() {
    UserDomain userMock = mock(UserDomain.class);
    BigDecimal balance = BigDecimal.valueOf(1000);
    BalanceDomain januaryBalance = new BalanceDomain(
        balance,
        userMock,
        Short.valueOf("1"),
        Short.valueOf("2025")
    );

    when(this.searchBalanceRepositoryPort.userBalanceForMonthYear(anyLong(), anyShort(), anyShort()))
        .thenReturn(Optional.of(januaryBalance));

    BalanceDomain balanceToAdd = new BalanceDomain(
        BigDecimal.TEN,
        userMock,
        Short.valueOf("1"),
        Short.valueOf("2025")
    );
    this.updateBalanceOfMonthUseCase.execute(balanceToAdd);

    assertEquals(BigDecimal.valueOf(1010), januaryBalance.getAmount());
    verify(this.searchBalanceRepositoryPort, times(1))
        .userBalanceForMonthYear(anyLong(), anyShort(), anyShort());
    verify(this.saveBalanceRepositoryPort, times(1))
        .one(januaryBalance);
  }

  @Test
  @DisplayName("should create a balance for the month when does not exists balance for this month informed")
  void shouldCreateABalanceForTheMonthWhenDoesNotExistsBalanceForThisMonthInformed() {
    UserDomain userMock = mock(UserDomain.class);
    when(this.searchBalanceRepositoryPort.userBalanceForMonthYear(anyLong(), anyShort(), anyShort()))
        .thenReturn(Optional.empty());
    BalanceDomain januaryBalance = new BalanceDomain(
        BigDecimal.TEN,
        userMock,
        Short.valueOf("1"),
        Short.valueOf("2025")
    );
    this.updateBalanceOfMonthUseCase.execute(januaryBalance);

    verify(this.searchBalanceRepositoryPort, times(1))
        .userBalanceForMonthYear(anyLong(), anyShort(), anyShort());
    verify(this.saveBalanceRepositoryPort, times(1))
        .one(januaryBalance);
  }
}
