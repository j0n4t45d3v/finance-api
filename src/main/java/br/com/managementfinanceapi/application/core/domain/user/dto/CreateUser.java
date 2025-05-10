package br.com.managementfinanceapi.application.core.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record CreateUser(
    @Email
    String email,
    @Size(min = 6, max = 20, message = "A senha deve conter entre 6 a 20 caracteres")
    String password,
    String confirmPassword
) {

  public boolean validatePassword() {
    return this.matchPasswords() && this.patternPassword();
  }

  private boolean matchPasswords() {
    return password.equals(confirmPassword);
  }

  private boolean patternPassword() {
    return true;
  }

}
