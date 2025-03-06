package br.com.managementfinanceapi.user.gateways;

import br.com.managementfinanceapi.user.domain.dto.UserResponse;

public interface FindOneUser {
  UserResponse byEmail(String email);
  UserResponse byId(Long id);
}
