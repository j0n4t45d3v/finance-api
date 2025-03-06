package br.com.managementfinanceapi.user.gateways;

public interface ChangePassword {
  void change(String email, String password);
}
