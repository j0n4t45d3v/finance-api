package br.com.managementfinanceapi.application.core.usecase.transaction;

import br.com.managementfinanceapi.application.core.domain.common.dvo.DateRange;
import br.com.managementfinanceapi.application.core.domain.common.dvo.PageDto;
import br.com.managementfinanceapi.application.core.domain.common.exception.InvalidDateRangeException;
import br.com.managementfinanceapi.application.core.domain.common.exception.NotFoundException;
import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;
import br.com.managementfinanceapi.application.core.domain.transaction.dvo.SearchAllFilters;
import br.com.managementfinanceapi.application.core.domain.transaction.enums.TransactionType;
import br.com.managementfinanceapi.application.port.out.transaction.SearchTransactionRespositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class SearchTransactionUseCaseTest {

  @Mock
  private SearchTransactionRespositoryPort searchRepositoryPort;
  @InjectMocks
  private SearchTransactionUseCase searchTransactionUseCase;

  @Test
  @DisplayName("should return page with transactions")
  void shouldReturnPageWithTransactions() {
    DateRange dateRange = new DateRange(LocalDate.now(), LocalDate.now());
    SearchAllFilters filters = new SearchAllFilters(1L, dateRange, TransactionType.INCOME, null);
    List<TransactionDomain> contentPageMock = List.of(mock(TransactionDomain.class));

    Mockito.when(this.searchRepositoryPort.all(any(SearchAllFilters.class)))
        .thenReturn(new PageDto<>(contentPageMock, 0, 5, 1));

    PageDto<TransactionDomain> transactionDomainPage = this.searchTransactionUseCase.all(filters);

    assertEquals(0, transactionDomainPage.page());
    assertEquals(5, transactionDomainPage.size());
    assertEquals(1, transactionDomainPage.total());
    assertEquals(1, transactionDomainPage.content().size());
  }

  @Test
  @DisplayName("should return all user transactions in date range")
  void shouldReturnAllUserTransactionsInDateRange() {
    DateRange dateRange = new DateRange(LocalDate.now(), LocalDate.now());
    List<TransactionDomain> contentMock = List.of(mock(TransactionDomain.class));

    Mockito.when(this.searchRepositoryPort.allByUser(anyLong(), any(DateRange.class)))
        .thenReturn(contentMock);

    List<TransactionDomain> transactions = this.searchTransactionUseCase.allByUser(1L, dateRange);

    assertEquals(1, transactions.size());
  }

  @Test
  @DisplayName("should throw invalid date when date range is null")
  void shouldThrowInvalidDateWhenDateRangeIsNull() {
    InvalidDateRangeException thrown = assertThrows(
        InvalidDateRangeException.class,
        () -> this.searchTransactionUseCase.allByUser(1L, null)
    ) ;
    assertEquals(400, thrown.getCode());
    assertEquals("Periodo de data não informado!", thrown.getMessage());
  }

  @Test
  @DisplayName("should throw invalid date when start date is greater than the end date")
  void shouldThrowInvalidDateWhenStartDateIsGreaterThanTheEndDate() {
    DateRange dateRange = new DateRange(LocalDate.of(2026, 1, 1), LocalDate.of(2025, 1, 1));
    InvalidDateRangeException thrown = assertThrows(
        InvalidDateRangeException.class,
        () -> this.searchTransactionUseCase.allByUser(1L, dateRange)
    ) ;
    assertEquals(400, thrown.getCode());
    assertEquals("Data inicial é menor que a data final!", thrown.getMessage());
  }

  @Test
  @DisplayName("should return transaction by id when there is a transaction with the id")
  void shouldReturnTransactionByIdWhenThereIsATransactionWithTheId() {
    Mockito.when(this.searchRepositoryPort.byId(anyLong()))
        .thenReturn(Optional.of(mock(TransactionDomain.class)));
    TransactionDomain transactionDomain = this.searchTransactionUseCase.byId(1L);
    assertNotNull(transactionDomain);
  }

  @Test
  @DisplayName("should throw transaction not found when transaction id does not exists")
  void shouldThrowTransactionNotFoundWhenTransactionIdDoesNotExists() {
    Mockito.when(this.searchRepositoryPort.byId(anyLong()))
        .thenReturn(Optional.empty());
    NotFoundException thrown = assertThrows(NotFoundException.class, () -> this.searchTransactionUseCase.byId(1L));
    assertEquals(404, thrown.getCode());
    assertEquals("Transação não encontrada", thrown.getMessage());
  }
}