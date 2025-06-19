package br.com.managementfinanceapi.application.core.usecase.category;

import br.com.managementfinanceapi.application.core.domain.category.CategoryDomain;
import br.com.managementfinanceapi.application.core.domain.category.exception.CategoryDoesNotExistsException;
import br.com.managementfinanceapi.application.port.out.category.SearchCategoryRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
}
