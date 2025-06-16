package br.com.managementfinanceapi.adapter.in.dto.user;

import br.com.managementfinanceapi.application.core.domain.user.UserDomain;

public record UserResponse(
    Long id,
    String email
) {
  public static UserResponse from(UserDomain entity) {
    return new UserResponse(entity.getId(), entity.getEmail());
  }
}