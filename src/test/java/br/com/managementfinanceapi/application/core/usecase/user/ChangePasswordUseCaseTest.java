package br.com.managementfinanceapi.application.core.usecase.user;

import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.application.core.domain.user.dvo.Password;
import br.com.managementfinanceapi.application.core.domain.user.exception.UserNotFound;
import br.com.managementfinanceapi.application.port.in.user.SearchUserPort;
import br.com.managementfinanceapi.application.port.out.security.HashPasswordPort;
import br.com.managementfinanceapi.application.port.out.user.SaveUserPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChangePasswordUseCaseTest {

  @Mock
  private SearchUserPort searchUserPort;
  @Mock
  private HashPasswordPort hashPasswordPort;
  @Mock
  private SaveUserPort saveUserPort;
  @InjectMocks
  private ChangePasswordUseCase changePasswordUseCase;

  @Test
  @DisplayName("should change password")
  void shouldChangePassword() {
    UserDomain userFound = new UserDomain(1L, "john@doe.com", Password.fromEncoded("EncodedPassword"));

    Mockito.when(this.searchUserPort.byEmail(anyString()))
        .thenReturn(userFound);
    Mockito.when(this.hashPasswordPort.encode(any(Password.class)))
        .thenReturn("EncodedPasswordChanged");

    this.changePasswordUseCase.change("john@doe.com", "johnDoeChanged");

    assertEquals("EncodedPasswordChanged", userFound.getPassword());

    verify(this.searchUserPort, times(1))
        .byEmail(anyString());
    verify(this.hashPasswordPort, times(1))
        .encode(any(Password.class));
    verify(this.saveUserPort, times(1))
        .execute(any(UserDomain.class));
  }

  @Test
  @DisplayName("should throw user not found when the user not exists")
  void shouldThrowUserNotFoundWhenTheUserNotExists() {

    Mockito.when(this.searchUserPort.byEmail(anyString()))
        .thenThrow(new UserNotFound());

    UserNotFound thrown = assertThrows(UserNotFound.class, () -> this.changePasswordUseCase.change("john@doe.com", "johnDoeChanged"))   ;

    assertEquals(404, thrown.getCode());
    assertEquals("Usuario não encontrado!", thrown.getMessage());

    verify(this.searchUserPort, times(1))
        .byEmail(anyString());
    verify(this.hashPasswordPort, times(0))
        .encode(any(Password.class));
    verify(this.saveUserPort, times(0))
        .execute(any(UserDomain.class));
  }
}
