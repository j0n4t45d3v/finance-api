package br.com.managementfinanceapi.application.core.domain.user.dvo;

import br.com.managementfinanceapi.application.core.domain.user.exception.InvalidPassword;
import br.com.managementfinanceapi.application.port.out.security.HashPasswordPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {

  @Test
  @DisplayName("should create password object when password raw is valid")
  void shouldCreatePasswordObjectWhenPasswordRawIsValid() {
    String passwordRaw = "test123test";
    Password passwordRepresentation = Password.fromRaw(passwordRaw);
    assertEquals(passwordRaw, passwordRepresentation.getValue());
  }

  @Test
  @DisplayName("should throw password cannot be empty")
  void shouldThrowPasswordDoesNotIsEmpty() {
    String passwordRawEmpty = "";
    InvalidPassword thrownFirstCase = assertThrows(InvalidPassword.class, () -> Password.fromRaw(passwordRawEmpty));
    InvalidPassword thrownSecondCase = assertThrows(InvalidPassword.class, () -> Password.fromRaw(null));
    assertEquals("Nenhuma senha informada", thrownFirstCase.getMessage());
    assertEquals("Nenhuma senha informada", thrownSecondCase.getMessage());
  }

  @Test
  @DisplayName("should throw invalid size if password length is less to 8 or greater to 20")
  void shouldThrowInvalidSizeIfPasswordLengthIsLessToEightOrGreaterToTwenty() {
    String shortPassword = "short";
    String longPassword = "longpasswordlongpasswordlongpasswordlongpasswordlongpassword";
    InvalidPassword thrownFirstCase = assertThrows(InvalidPassword.class, () -> Password.fromRaw(shortPassword));
    InvalidPassword thrownSecondCase = assertThrows(InvalidPassword.class, () -> Password.fromRaw(longPassword));
    assertEquals("A senha deve ter pelo menos de 8 á 20 caracteres!", thrownFirstCase.getMessage());
    assertEquals("A senha deve ter pelo menos de 8 á 20 caracteres!", thrownSecondCase.getMessage());
  }


  @Test
  @DisplayName("should return new password object with the password encoded")
  void shouldReturnNewPasswordObjectWithThePasswordEncoded() {
    HashPasswordPort hashPasswordMock = Mockito.mock(HashPasswordPort.class);
    Password password = Password.fromRaw("test123test");
    Mockito.when(hashPasswordMock.encode(password))
        .thenReturn("encodedPassword");
    Password passwordEncoded = password.encode(hashPasswordMock);
    assertNotEquals(password, passwordEncoded);
    assertEquals("encodedPassword", passwordEncoded.getValue());
    assertEquals("test123test", password.getValue());
  }
}