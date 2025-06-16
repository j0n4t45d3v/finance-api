package br.com.managementfinanceapi.application.core.usecase.user;

import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.application.core.domain.user.dvo.Password;
import br.com.managementfinanceapi.application.core.domain.user.exception.EmailAlreadyUsed;
import br.com.managementfinanceapi.application.core.domain.user.exception.InvalidPassword;
import br.com.managementfinanceapi.application.port.out.security.HashPasswordPort;
import br.com.managementfinanceapi.application.port.out.user.FindUserPort;
import br.com.managementfinanceapi.application.port.out.user.SaveUserPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

  @Mock
  private SaveUserPort saveUserPort;
  @Mock
  private FindUserPort findUserPort;
  @Mock
  private HashPasswordPort hashPasswordPort;
  @InjectMocks
  private RegisterUserUseCase registerUserUseCase;

  @Test
  @DisplayName("should register new user when user not exists in database")
  void shouldRegisterNewUserWhenUserNotExistsInDatabase() {
    UserDomain domain = new UserDomain("john@doe.com", Password.fromRaw("johndoe123"));

    Mockito.when(this.findUserPort.byEmail(anyString()))
        .thenReturn(Optional.empty());
    Mockito.when(this.hashPasswordPort.encode(any(Password.class)))
        .thenReturn("encodedpassword");

    Mockito.when(this.saveUserPort.execute(any(UserDomain.class)))
        .thenReturn(new UserDomain(1L, "john@doe.com", Password.fromRaw("encodedpassword")));

    Mockito.when(this.hashPasswordPort.matchers(anyString(), anyString()))
        .thenReturn(true);

    UserDomain result = this.registerUserUseCase.execute(domain, "johndoe123");
    assertEquals(1L, result.getId());
    assertEquals("john@doe.com", result.getEmail());
    assertEquals("encodedpassword", result.getPassword());
    assertEquals("encodedpassword", domain.getPassword());

    Mockito.verify(this.saveUserPort, Mockito.times(1))
        .execute(any(UserDomain.class));
  }

  @Test
  @DisplayName("should throw email already exists when the email is already being used by another user")
  void shouldThrowEmailAlreadyExistsWhenTheEmailIsAlreadyBeingUsedByAnotherUser() {
    UserDomain domain = new UserDomain("john@doe.com", Password.fromRaw("johndoe123"));

    UserDomain mockUser = Mockito.mock(UserDomain.class);
    Mockito.when(this.findUserPort.byEmail(anyString()))
        .thenReturn(Optional.of(mockUser));

    EmailAlreadyUsed thrown = assertThrows(EmailAlreadyUsed.class, () -> this.registerUserUseCase.execute(domain, "johndoe123"));
    assertEquals(409, thrown.getCode());
    assertEquals("E-mail já está sendo usado por outro usuário!", thrown.getMessage());

    Mockito.verify(this.saveUserPort, Mockito.times(0))
        .execute(any(UserDomain.class));
  }

  @Test
  @DisplayName("should throw invalid password when the password and confirm password has distinct")
  void shouldThrowInvalidPasswordWhenThePasswordAndConfirmPasswordHasDistincts() {
    UserDomain domain = new UserDomain("john@doe.com", Password.fromRaw("johndoe123"));

    Mockito.when(this.findUserPort.byEmail(anyString()))
        .thenReturn(Optional.empty());

    Mockito.when(this.hashPasswordPort.encode(any(Password.class)))
        .thenReturn("encodedpassword");

    Mockito.when(this.hashPasswordPort.matchers(anyString(), anyString()))
        .thenReturn(false);

    InvalidPassword thrown = assertThrows(InvalidPassword.class, () -> this.registerUserUseCase.execute(domain, "johndoe123"));
    assertEquals(400, thrown.getCode());
    assertEquals("As senhas são divergentes", thrown.getMessage());

    Mockito.verify(this.saveUserPort, Mockito.times(0))
        .execute(any(UserDomain.class));
  }
}