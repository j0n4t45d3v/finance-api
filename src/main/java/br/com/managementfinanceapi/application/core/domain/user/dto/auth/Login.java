package br.com.managementfinanceapi.application.core.domain.user.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record Login(
  @NotNull(message = "E-mail é um campo obrigátorio")
  @Email(message = "E-mail inválido")
  String email,
  String password
) { }