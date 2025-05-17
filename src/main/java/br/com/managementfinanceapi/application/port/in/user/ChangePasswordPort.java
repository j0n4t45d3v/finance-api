package br.com.managementfinanceapi.application.port.in.user;

public interface ChangePasswordPort {
  void change(String email, String password);
}
