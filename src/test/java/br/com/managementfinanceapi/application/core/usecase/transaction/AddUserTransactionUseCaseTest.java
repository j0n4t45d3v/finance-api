package br.com.managementfinanceapi.application.core.usecase.transaction;

import br.com.managementfinanceapi.application.core.domain.category.CategoryDomain;
import br.com.managementfinanceapi.application.core.domain.transaction.BalanceDomain;
import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;
import br.com.managementfinanceapi.application.core.domain.transaction.enums.TransactionType;
import br.com.managementfinanceapi.application.port.in.transaction.CreateTransactionPort;
import br.com.managementfinanceapi.application.port.in.transaction.UpdateBalanceOfMonthPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AddUserTransactionUseCaseTest {

  @Mock
  private CreateTransactionPort createTransactionPort;
  @Mock
  private UpdateBalanceOfMonthPort updateBalanceOfMonthPort;
  @Mock
  private UpdateBalanceForSubsequenceMonthsUseCase updateBalanceForSubsequenceMonthsUseCase;
  @InjectMocks
  private AddUserTransactionUseCase addUserTransactionUseCase;

  @Test
  @DisplayName("should add a new transaction and update balance")
  void shouldAddANewTransactionAndUpdateBalance() {
    TransactionDomain newTransaction = new TransactionDomain(
        null,
        BigDecimal.TEN,
        TransactionType.INCOME,
        "test",
        LocalDateTime.now(),
        null,
        new CategoryDomain(1L)
    );

    this.addUserTransactionUseCase.execute(newTransaction, 1L);

    verify(this.createTransactionPort, times(1))
        .execute(any(TransactionDomain.class), anyLong());

    verify(this.updateBalanceOfMonthPort, times(1))
        .execute(any(BalanceDomain.class));

    verify(this.updateBalanceForSubsequenceMonthsUseCase, times(1))
        .execute(anyLong(), any(BigDecimal.class), any(YearMonth.class));
  }

}