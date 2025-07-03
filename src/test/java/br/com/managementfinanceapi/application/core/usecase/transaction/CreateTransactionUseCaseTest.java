package br.com.managementfinanceapi.application.core.usecase.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.managementfinanceapi.application.core.domain.category.exception.CategoryDoesNotExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.managementfinanceapi.application.core.domain.category.CategoryDomain;
import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;
import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.application.core.domain.user.exception.UserNotFound;
import br.com.managementfinanceapi.application.port.in.category.SearchCategoryPort;
import br.com.managementfinanceapi.application.port.in.user.SearchUserPort;
import br.com.managementfinanceapi.application.port.out.transaction.SaveTransactionRepositoryPort;

@ExtendWith(MockitoExtension.class)
public class CreateTransactionUseCaseTest {
  
  @Mock
  private SaveTransactionRepositoryPort saveTransactionRepositoryPort;
  @Mock
  private SearchUserPort searchUserPort;
  @Mock
  private SearchCategoryPort searchCategoryPort;
  @InjectMocks
  private CreateTransactionUseCase createTransactionUseCase;

  @Test
  @DisplayName("should create a transaction when the user exists")
  void shouldCreateATransactionWhenTheUserExists() {
    UserDomain userMock = mock(UserDomain.class);
    CategoryDomain categoryMock = mock(CategoryDomain.class);
    TransactionDomain transaction = mock(TransactionDomain.class);

    when(this.searchUserPort.byId(anyLong()))
      .thenReturn(userMock);

    when(this.searchCategoryPort.byId(anyLong(), anyLong()))
      .thenReturn(categoryMock);

    this.createTransactionUseCase.execute(transaction, 1L);

    verify(transaction, times(1))
      .signUser(any(UserDomain.class));
    verify(this.searchUserPort, times(1))
      .byId(anyLong());
    verify(this.searchCategoryPort, times(1))
      .byId(anyLong(), anyLong());
    verify(this.saveTransactionRepositoryPort, times(1))
      .execute(any(TransactionDomain.class));
  }

  @Test
  @DisplayName("should throw user not found when the user not exists")
  void shouldThrowUserNotFoundWhenTheUserNotExists() {
    TransactionDomain transaction = mock(TransactionDomain.class);

    when(this.searchUserPort.byId(anyLong()))
      .thenThrow(new UserNotFound());

    UserNotFound thrown = assertThrows(
      UserNotFound.class,
      () -> this.createTransactionUseCase.execute(transaction, 1L)
    );

    assertEquals(404, thrown.getCode());
    assertEquals("Usuario não encontrado!", thrown.getMessage());

    verify(transaction, times(0))
      .signUser(any(UserDomain.class));
    verify(this.searchUserPort, times(1))
      .byId(anyLong());
    verify(this.searchCategoryPort, times(0))
      .byId(anyLong(), anyLong());
    verify(this.saveTransactionRepositoryPort, times(0))
      .execute(any(TransactionDomain.class));
  }

  @Test
  @DisplayName("should throw category found when the category not exists for the user")
  void shouldThrowCategoryNotFoundWhenTheCategoryNotExistsForTheUser() {
    UserDomain userMock = mock(UserDomain.class);
    TransactionDomain transaction = mock(TransactionDomain.class);

    when(this.searchUserPort.byId(anyLong()))
        .thenReturn(userMock);

    when(this.searchCategoryPort.byId(anyLong(), anyLong()))
        .thenThrow(new CategoryDoesNotExistsException());

    CategoryDoesNotExistsException thrown = assertThrows(
        CategoryDoesNotExistsException.class,
        () -> this.createTransactionUseCase.execute(transaction, 1L)
    );

    assertEquals(404, thrown.getCode());
    assertEquals("Categoria não existe!", thrown.getMessage());

    verify(transaction, times(1))
        .signUser(any(UserDomain.class));
    verify(this.searchUserPort, times(1))
        .byId(anyLong());
    verify(this.searchCategoryPort, times(1))
        .byId(anyLong(), anyLong());
    verify(this.saveTransactionRepositoryPort, times(0))
        .execute(any(TransactionDomain.class));
  }
}