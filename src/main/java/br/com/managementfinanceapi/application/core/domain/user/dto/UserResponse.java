package br.com.managementfinanceapi.application.core.domain.user.dto;

import br.com.managementfinanceapi.application.core.domain.user.User;

public record UserResponse(
    Long id,
    String email
) {
  public static UserResponse from(User entity) {
    return new UserResponse(entity.getId(), entity.getEmail());
  }
}
