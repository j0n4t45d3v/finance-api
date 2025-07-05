package br.com.managementfinanceapi.application.core.usecase.transaction;

import br.com.managementfinanceapi.application.core.domain.transaction.BalanceDomain;
import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.application.port.in.transaction.UpdateBalanceOfMonthPort;
import br.com.managementfinanceapi.application.port.out.transaction.SaveBalanceRepositoryPort;
import br.com.managementfinanceapi.application.port.out.transaction.SearchBalanceRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class UpdateBalanceForSubsequenceMonthsUseCaseTest {

  @Mock
  private SaveBalanceRepositoryPort saveBalanceRepositoryPort;
  @Mock
  private SearchBalanceRepositoryPort searchBalanceRepositoryPort;
  @InjectMocks
  private UpdateBalanceForSubsequenceMonthsUseCase updateBalanceForSubsequenceMonthsUseCase;

  @Test
  @DisplayName("should update user balances when there are months after the month informed")
  void shouldUpdateUserBalancesWhenThereAreMonthsAfterTheMonthInformed() {
    BalanceDomain balanceFebruary = new BalanceDomain(1L, BigDecimal.TEN, new UserDomain(1L), (short)2, (short)2025);
    BalanceDomain balanceMarch = new BalanceDomain(2L, BigDecimal.TEN, new UserDomain(1L), (short)3, (short)2025);
    Mockito.when(this.searchBalanceRepositoryPort.userBalancesAfterMonthYear(anyShort(), anyShort(), anyLong()))
        .thenReturn(List.of(balanceFebruary, balanceMarch));

    this.updateBalanceForSubsequenceMonthsUseCase.execute(1L, BigDecimal.TEN, YearMonth.of(2025 ,1));

    assertEquals(BigDecimal.valueOf(20), balanceFebruary.getAmount());
    assertEquals(BigDecimal.valueOf(20), balanceMarch.getAmount());

    Mockito.verify(this.searchBalanceRepositoryPort, Mockito.times(1))
        .userBalancesAfterMonthYear(anyShort(), anyShort(), anyLong());
    Mockito.verify(this.saveBalanceRepositoryPort, Mockito.times(1))
        .all(anyList());

  }


}