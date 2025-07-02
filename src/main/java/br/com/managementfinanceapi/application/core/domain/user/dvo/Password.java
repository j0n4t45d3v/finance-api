package br.com.managementfinanceapi.application.core.domain.user.dvo;

import br.com.managementfinanceapi.application.core.domain.user.exception.InvalidPassword;
import br.com.managementfinanceapi.application.port.out.security.HashPasswordPort;

public class Password {

  private final String value;

  private Password(String value) {
    this.value = value;
  }

  public static Password fromRaw(String password) {
    if(Password.isEmptyPassword(password)) {
      throw new InvalidPassword("Nenhuma senha informada");
    } else if(password.length() < 8 || password.length() > 20) {
      throw new InvalidPassword();
    }
    return new Password(password);
  }

  private static boolean isEmptyPassword(String password) {
    return password == null || password.isEmpty();
  }

  public static Password fromEncoded(String password) {
    return new Password(password);
  }

  public String getValue() {
    return value;
  }

  public Password encode(HashPasswordPort encoderStrategy) {
    return Password.fromEncoded(encoderStrategy.encode(this));
  }

}