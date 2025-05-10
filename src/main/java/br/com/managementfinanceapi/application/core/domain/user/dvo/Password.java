package br.com.managementfinanceapi.application.core.domain.user.dvo;

import br.com.managementfinanceapi.infra.error.exceptions.user.InvalidPassword;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Embeddable
public class Password {

  private static final PasswordEncoder encoder = new BCryptPasswordEncoder();
  @Column(name = "password")
  private String value;

  private Password(String value) {
    this.value = this.encode(value);
  }

  public Password() {
  }

  public static Password from(String password) {
    if(password == null) {
      throw new InvalidPassword("Nenhuma senha informada");
    }else if(password.length() < 8 || password.length() > 20) {
      throw new InvalidPassword();
    }
    return new Password(password);
  }

  private String encode(String value) {
    return encoder.encode(value);
  }

  public String getValue() {
    return value;
  }

  public boolean matches(String value) {
    return encoder.matches(value, this.value);
  }
}
