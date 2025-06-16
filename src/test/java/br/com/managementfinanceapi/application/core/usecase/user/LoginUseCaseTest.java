package br.com.managementfinanceapi.application.core.usecase.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.application.core.domain.user.dvo.Password;
import br.com.managementfinanceapi.application.core.domain.user.exception.InvalidCredentials;
import br.com.managementfinanceapi.application.core.domain.user.exception.UserNotFound;
import br.com.managementfinanceapi.application.port.in.user.SearchUserPort;
import br.com.managementfinanceapi.application.port.out.security.HashPasswordPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

  @Mock
  private SearchUserPort searchUserPort;
  @Mock
  private HashPasswordPort hashPasswordPort;
  @InjectMocks
  private LoginUseCase loginUseCase;

  @Test
  @DisplayName("should return the user when the credentials provided are correct")
  void shouldReturnTheUserWhenTheCredentialsProvidedAreCorrect() {
    UserDomain credentials = new UserDomain("john@doe.com", Password.fromRaw("john@doe"));
    UserDomain userFound = new UserDomain(1L, "john@doe.com", Password.fromEncoded("EncodedPassword"));

    Mockito.when(this.searchUserPort.byEmail(anyString()))
        .thenReturn(userFound);
    Mockito.when(this.hashPasswordPort.matchers(anyString(), anyString()))
        .thenReturn(true);

    UserDomain userLogged = this.loginUseCase.execute(credentials);
    assertNotNull( userLogged);
    assertEquals("john@doe.com", userLogged.getEmail());
  }

  @Test
  @DisplayName("should throw invalid credentials when the user not exists in database")
  void shouldThrowInvalidCredentialsWhenTheUserNotExistsInDatabase() {
    UserDomain credentials = new UserDomain("john@doe.com", Password.fromRaw("john@doe"));
    Mockito.when(this.searchUserPort.byEmail(anyString())).thenThrow(new UserNotFound());

    InvalidCredentials thrown = assertThrows(InvalidCredentials.class, () -> this.loginUseCase.execute(credentials));
    assertEquals(400, thrown.getCode());
    assertEquals("Usuário ou senha inválida", thrown.getMessage());
  }

  @Test
  @DisplayName("should throw invalid credentials when password is different from the record")
  void shouldThrowInvalidCredentialsWhenPasswordIsDifferentFromTheRecord() {
    UserDomain credentials = new UserDomain("john@doe.com", Password.fromRaw("john@doe"));
    UserDomain userFound = new UserDomain(1L, "john@doe.com", Password.fromEncoded("EncodedPasswordDiff"));

    Mockito.when(this.searchUserPort.byEmail(anyString()))
        .thenReturn(userFound);
    Mockito.when(this.hashPasswordPort.matchers(anyString(), anyString()))
        .thenReturn(false);

    InvalidCredentials thrown = assertThrows(InvalidCredentials.class, () -> this.loginUseCase.execute(credentials));
    assertEquals(400, thrown.getCode());
    assertEquals("Usuário ou senha inválida", thrown.getMessage());
  }
}