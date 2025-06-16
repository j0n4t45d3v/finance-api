package br.com.managementfinanceapi.application.core.usecase.user;

import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.application.core.domain.user.dvo.Password;
import br.com.managementfinanceapi.application.core.domain.user.exception.UserNotFound;
import br.com.managementfinanceapi.application.port.out.user.FindUserPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SearchUseCaseTest {

  @Mock
  private FindUserPort findUserPort;
  @InjectMocks
  private SearchUseCase searchUseCase;

  @Test
  @DisplayName("should return user by email")
  void shouldReturnUserByEmail() {
    UserDomain user = new UserDomain(1L, "john@doe.com", Password.fromEncoded("EncodedPassword"));
    Mockito.when(this.findUserPort.byEmail(anyString()))
            .thenReturn(Optional.of(user));
    Optional<UserDomain> result = this.findUserPort.byEmail("john@doe.com");
    assertTrue(result.isPresent());
    verify(this.findUserPort, times(1)).byEmail(anyString());
  }

  @Test
  @DisplayName("should throw user not found when email does not exists in database")
  void shouldThrowUserNotFoundWhenEmailDoesNotExistsInDatabase() {
    Mockito.when(this.findUserPort.byEmail(anyString()))
        .thenReturn(Optional.empty());
    UserNotFound thrown = assertThrows(UserNotFound.class,() -> this.searchUseCase.byEmail("john@doe.com"));
    assertEquals(404, thrown.getCode());
    assertEquals("Usuario não encontrado!", thrown.getMessage());
    verify(this.findUserPort, times(1)).byEmail(anyString());
  }

  @Test
  @DisplayName("should return user by id")
  void shouldReturnUserById() {
    UserDomain user = new UserDomain(1L, "john@doe.com", Password.fromEncoded("EncodedPassword"));
    Mockito.when(this.findUserPort.byId(anyLong()))
        .thenReturn(Optional.of(user));
    Optional<UserDomain> result = this.findUserPort.byId(1L);
    assertTrue(result.isPresent());
    verify(this.findUserPort, times(1))
        .byId(anyLong());
  }

  @Test
  @DisplayName("should throw user not found when id does not exists in database")
  void shouldThrowUserNotFoundWhenIdDoesNotExistsInDatabase() {
    Mockito.when(this.findUserPort.byId(anyLong()))
        .thenReturn(Optional.empty());
    UserNotFound thrown = assertThrows(UserNotFound.class,() -> this.searchUseCase.byId(1L));
    assertEquals(404, thrown.getCode());
    assertEquals("Usuario não encontrado!", thrown.getMessage());
    verify(this.findUserPort, times(1))
        .byId(anyLong());
  }
}