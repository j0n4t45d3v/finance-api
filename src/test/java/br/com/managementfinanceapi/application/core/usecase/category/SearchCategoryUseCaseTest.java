package br.com.managementfinanceapi.application.core.usecase.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.managementfinanceapi.application.core.domain.category.CategoryDomain;
import br.com.managementfinanceapi.application.core.domain.category.dvo.CategoryTransactionSummary;
import br.com.managementfinanceapi.application.core.domain.category.exception.CategoryDoesNotExistsException;
import br.com.managementfinanceapi.application.core.domain.common.dvo.DateRange;
import br.com.managementfinanceapi.application.port.out.category.SearchCategoryRepositoryPort;

@ExtendWith(MockitoExtension.class)
class SearchCategoryUseCaseTest {

  @Mock
  private SearchCategoryRepositoryPort searchCategoryRepositoryPort;
  @InjectMocks
  private SearchCategoryUseCase searchCategoryUseCase;

  @Test
  @DisplayName("should return all user categories")
  void shouldReturnAllUserCategories() {
    when(this.searchCategoryRepositoryPort.all()).thenReturn(List.of(new CategoryDomain(1L)));
    List<CategoryDomain> result = this.searchCategoryUseCase.all();
    assertEquals(1, result.size());
    assertEquals(1L, result.get(0).getId());
    verify(this.searchCategoryRepositoryPort, times(1)).all();
  }

  @Test
  @DisplayName("should return user category when category name exists")
  void shouldReturnUserCategoryWhenCategoryNameExists() {
    when(this.searchCategoryRepositoryPort.byUserIdAndName(anyLong(), anyString()))
        .thenReturn(Optional.of(new CategoryDomain(1L)));
    CategoryDomain result = this.searchCategoryUseCase.byName(1L, "Teste category");
    assertEquals(1L, result.getId());
    verify(this.searchCategoryRepositoryPort, times(1))
        .byUserIdAndName(anyLong(), anyString());
  }

  @Test
  @DisplayName("should throw category does not exists when category name does not exists for user")
  void shouldThrowCategoryDoesNotExistsWhenCategoryNameDoesNotExistsForUser() {
    when(this.searchCategoryRepositoryPort.byUserIdAndName(anyLong(), anyString()))
        .thenReturn(Optional.empty());

    CategoryDoesNotExistsException thrown = assertThrows(CategoryDoesNotExistsException.class, () ->this.searchCategoryUseCase.byName(1L, "Teste category") );
    assertEquals(404, thrown.getCode());
    assertEquals("Categoria não existe!", thrown.getMessage());
    verify(this.searchCategoryRepositoryPort, times(1))
        .byUserIdAndName(anyLong(), anyString());
  }

  @Test
  @DisplayName("should return all transaction grouped by category")
  void shouldReturnAllTransactionGroupedByCategory() {
    CategoryTransactionSummary categoryTransactionSummaryMock = mock(CategoryTransactionSummary.class);
    when(this.searchCategoryRepositoryPort.getSummaryIncomeAndExpencesTotals(
        anyLong(),
        any(LocalDateTime.class),
        any(LocalDateTime.class)
      )).thenReturn(List.of(categoryTransactionSummaryMock));

    DateRange period = new DateRange(LocalDate.now(), LocalDate.now());
    List<CategoryTransactionSummary> result = 
      this.searchCategoryUseCase.getSummaryIncomeAndExpencesTotals(1L, period);

    assertEquals(1, result.size());
    assertNotNull(result.get(0));

    verify(this.searchCategoryRepositoryPort, times(1))
      .getSummaryIncomeAndExpencesTotals(
        anyLong(),
        any(LocalDateTime.class),
        any(LocalDateTime.class)
      );
  }
}