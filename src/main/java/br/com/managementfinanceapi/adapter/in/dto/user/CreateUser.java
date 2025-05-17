package br.com.managementfinanceapi.adapter.in.dto.user;

import br.com.managementfinanceapi.application.core.domain.user.UserDomain;
import br.com.managementfinanceapi.application.core.domain.user.dvo.Password;
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

  public UserDomain toDomain() {
    return new UserDomain(this.email(), Password.from(this.password()));
  }

}