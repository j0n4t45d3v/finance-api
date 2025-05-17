package br.com.managementfinanceapi.application.core.domain.user;

import br.com.managementfinanceapi.application.core.domain.user.dto.UserResponse;
import br.com.managementfinanceapi.application.core.domain.user.dvo.Password;

public class UserDomain {
  private Long id;
  private String email;
  private Password password;

  public UserDomain(Long id, String email, Password password) {
    this.id = id;
    this.email = email;
    this.password = password;
  }

  public UserDomain(String email, Password password) {
    this.email = email;
    this.password = password;
  }

  public UserDomain() {
  }

  public UserDomain(Long userId) {
    this.id = userId;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password.getValue();
  }

  public boolean notMatchPassword(String confirmPassword) {
    return !this.password.matches(confirmPassword);
  }

  public void changePassword(String password) {
    this.password = Password.from(password);
  }
}