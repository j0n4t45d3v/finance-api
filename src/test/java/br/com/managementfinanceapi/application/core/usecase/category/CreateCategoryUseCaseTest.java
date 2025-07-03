package br.com.managementfinanceapi.application.core.usecase.category;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import br.com.managementfinanceapi.application.core.domain.category.CategoryDomain;
import br.com.managementfinanceapi.application.core.domain.category.exception.CategoryAlreadyExistsException;
import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.application.port.out.category.ExistsCategoryRepositoryPort;
import br.com.managementfinanceapi.application.port.out.category.SaveCategoryRepositoryPort;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateCategoryUseCaseTest {

  @Mock
  private SaveCategoryRepositoryPort saveCategoryRepositoryPort;
  @Mock
  private ExistsCategoryRepositoryPort existsCategoryRepositoryPort;
  @InjectMocks
  private CreateCategoryUseCase createCategoryUseCase;
  @Mock
  private UserDomain userMock;

  @Test
  @DisplayName("should create a new category for the user when the user not have a category with the same name")
  void shouldCreateANewCategoryForTheUserWhenTheUserNotHaveACategoryWithTheSameName() {
    Mockito.when(userMock.getId()).thenReturn(1L);
    Mockito.when(this.existsCategoryRepositoryPort.byName(anyString())).thenReturn(false);
    Mockito.when(this.saveCategoryRepositoryPort.execute(any(CategoryDomain.class)))
        .thenReturn(new CategoryDomain(1L, "Test Category", BigDecimal.TEN, this.userMock));
    CategoryDomain category =
        new CategoryDomain(null, "Test Category", BigDecimal.TEN, this.userMock);

    CategoryDomain result = this.createCategoryUseCase.execute(category);

    assertEquals(1L, result.getId());
    assertEquals("Test Category", result.getName());
    assertEquals(1L, result.getUser().getId());

    Mockito.verify(this.saveCategoryRepositoryPort, Mockito.times(1))
        .execute(any(CategoryDomain.class));
  }

  @Test
  @DisplayName("should throw category already exists when the user have a category with the same name")
  void shouldThrowCategoryAlreadyWhenTheUserHaveACategoryWithTheSameName() {
    Mockito.when(this.existsCategoryRepositoryPort.byName(anyString())).thenReturn(true);
    CategoryDomain category =
        new CategoryDomain(null, "Test Category", BigDecimal.TEN, this.userMock);

    CategoryAlreadyExistsException thrown =
        assertThrows(CategoryAlreadyExistsException.class, () -> this.createCategoryUseCase.execute(category));

    assertEquals(409, thrown.getCode());
    assertEquals("Categoria já existe!", thrown.getMessage());

    Mockito.verify(this.saveCategoryRepositoryPort, Mockito.times(0))
        .execute(any(CategoryDomain.class));
  }
}
