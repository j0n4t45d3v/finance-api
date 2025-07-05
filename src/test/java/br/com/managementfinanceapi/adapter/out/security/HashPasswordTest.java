package br.com.managementfinanceapi.adapter.out.security;


import br.com.managementfinanceapi.application.core.domain.user.dvo.Password;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class HashPasswordTest {

  private HashPassword hashPassword;

  @BeforeEach
  void setUp() {
    this.hashPassword = new HashPassword(new BCryptPasswordEncoder());
  }

  @AfterEach
  void tealDown() {
    this.hashPassword = null;
  }

  @Test
  @DisplayName("should encode raw password")
  void shouldEncodeRawPassword() {
    Password passwordRaw = Password.fromRaw("teste123test");
    String passwordValueEncoded = this.hashPassword.encode(passwordRaw);
    assertNotEquals(passwordRaw.getValue(), passwordValueEncoded);
  }

  @Test
  @DisplayName("should return false when encoded password is different from the raw password")
  void shouldReturnFalseWhenEncodedPasswordIsDifferentFromTheRawPassword() {
    Password passwordRaw = Password.fromRaw("teste123test");
    String passwordValueEncoded = this.hashPassword.encode(passwordRaw);
    boolean matchers = this.hashPassword.matchers(passwordValueEncoded, "differentPassword");
    assertFalse(matchers);
  }

  @Test
  @DisplayName("should return true when password match")
  void shouldReturnTrueWhenPasswordMatch() {
    Password passwordRaw = Password.fromRaw("teste123test");
    String passwordValueEncoded = this.hashPassword.encode(passwordRaw);
    boolean matchers = this.hashPassword.matchers(passwordValueEncoded, passwordRaw.getValue());
    assertTrue(matchers);
  }

}