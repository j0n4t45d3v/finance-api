package br.com.managementfinanceapi.application.port.out.security.jwt;

public interface GenerateTokenPort {

  String access(String subject);
  String refresh(String subject);

}