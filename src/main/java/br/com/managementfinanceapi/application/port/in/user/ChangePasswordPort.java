package br.com.managementfinanceapi.application.port.in.user;

public interface ChangePassword {
  void change(String email, String password);
}
