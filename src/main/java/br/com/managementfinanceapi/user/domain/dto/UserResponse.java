package br.com.managementfinanceapi.user.domain.dto;

import br.com.managementfinanceapi.user.domain.User;

public record UserResponse(
    Long id,
    String email
) {
  public static UserResponse from(User entity) {
    return new UserResponse(entity.getId(), entity.getEmail());
  }
}
